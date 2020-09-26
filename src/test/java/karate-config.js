function fn() {    
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  
  if (!env) {
    env = 'dev';
  }
  var config = {
    env: env,
//    QA_USERNAME: 'devuser',
//    QA_PASSWORD: 'devpass',
    QA_HOST: 'http://dummy.restapiexample.com/api/v1/',
    QA_AUTHENTICATION: 'create'
  }

  if (env == 'qa') {
//	config.QA_USERNAME: 'ravindra.karanki@retailmerchantservices.co.uk'
//	config.QA_PASSWORD: 'myPa55word'
	config.QA_HOST = 'http://54.171.72.116:8090/';
  } else if (env == 'e2e') {
    config.QA_HOST = 'http://dummy.restapiexample.com/api/v1/';
    config.API_GATEWAY = 'http://54.171.72.116:8888/';
  }

  karate.log('Open QA Host:', config.QA_HOST);
  karate.configure('connectTimeout', 60000);
  karate.configure('readTimeout', 60000);
  return config;
}