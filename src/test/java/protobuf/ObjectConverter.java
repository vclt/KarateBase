package protobuf;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import protobuf.utils.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ObjectConverter {

    private final ServiceResolver serviceResolver;

    public ObjectConverter(ServiceResolver serviceResolver) {
        this.serviceResolver = serviceResolver;
    }

    public byte[] jsonToProto(String type, String json) {
        return getTypeDescriptor(type)
                .map(x -> jsonToProto(x, json))
                .orElse(new byte[0]);
    }

    public byte[] jsonToProto(Descriptors.Descriptor descriptor, String json) {
        final JsonFormat.TypeRegistry typeRegistry = getTypeRegistry();

        final Map<String, Object> payload = new Gson().fromJson(json, Map.class);

        final ImmutableList<DynamicMessage> requestMessages = createDynamicMessages(descriptor, typeRegistry, payload);

        return requestMessages.stream()
                .map(AbstractMessageLite::toByteArray)
                .findFirst()
                .orElse(new byte[0]);
    }

    public String protoToJson(String type, byte[] data) throws InvalidProtocolBufferException {

        final Optional<Descriptors.Descriptor> descriptor = getTypeDescriptor(type);

        if (!descriptor.isPresent()) {
            return "";
        }

        return protoToJson(descriptor.get(), data);
    }

    private String protoToJson(Descriptors.Descriptor descriptor, byte[] data) throws InvalidProtocolBufferException {
        final JsonFormat.TypeRegistry typeRegistry = getTypeRegistry();

        final DynamicMessage dynamicMessage = DynamicMessage.parseFrom(descriptor, data);
        return JsonFormat.printer().usingTypeRegistry(typeRegistry).print(dynamicMessage);
    }

    private JsonFormat.TypeRegistry getTypeRegistry() {
        return JsonFormat.TypeRegistry.newBuilder().add(serviceResolver.listMessageTypes()).build();
    }

    private Optional<Descriptors.Descriptor> getTypeDescriptor(String type) {
        return serviceResolver.listMessageTypes()
                .stream()
                .filter(x -> type.equals(x.getFullName()))
                .findFirst();
    }

    private static ImmutableList<DynamicMessage> createDynamicMessages(Descriptors.Descriptor descriptor, JsonFormat.TypeRegistry registry, Map<String, Object> payload) {
        List<Map<String, Object>> payloads = new ArrayList<>();
        payloads.add(payload);
        return Reader.create(descriptor, payloads, registry).read();
    }

}
