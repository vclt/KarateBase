package KarateStructure;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import protobuf.ObjectConverter;
import protobuf.ServiceResolver;
import protobuf.utils.FileHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class JsonProtoBufConversions {

    private static ServiceResolver serviceResolver;
    private static ObjectConverter objectConverter;

    public static String ConvertProtoBufToJson(String type, byte[] payload) throws InvalidProtocolBufferException {
        return getObjectConverter().protoToJson(type, payload);
    }

    public static byte[] ConvertJsonToProtoBuf(String type, String jsonObject) throws InvalidProtocolBufferException {
        return getObjectConverter().jsonToProto(type, jsonObject);
    }

    private static ObjectConverter getObjectConverter() throws InvalidProtocolBufferException {
        if (objectConverter == null) {
            objectConverter = new ObjectConverter(getServiceResolver());
        }
        return objectConverter;
    }

    private static ServiceResolver getServiceResolver() throws InvalidProtocolBufferException {
        if (serviceResolver == null) {
            final byte[] data = getData();
            final DescriptorProtos.FileDescriptorSet fds = DescriptorProtos.FileDescriptorSet.parseFrom(data);
            serviceResolver = ServiceResolver.fromFileDescriptorSet(fds);
        }
        return serviceResolver;
    }

    private static byte[] getData() {

        final String path = "src/test/java/ISO20022/.karate-protobuf/protobuf-descriptor-sets/";
        final Path descriptorPath = Paths.get(path + "karate-protobuf.protobin");

        FileHelper.validatePath(Optional.of(descriptorPath));

        byte[] data;

        try {
            data = Files.readAllBytes(descriptorPath);
        } catch (IOException e) {
            throw new IllegalArgumentException("Read descriptor path failed: " + descriptorPath.toString());
        }

        return data;
    }

}
