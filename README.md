# Description

Application with two modules, for basic operations like as sum, subtract, multiply and division.
* The module **calculator** process the calc and respond the result of operation.
    * This module uses queues to process all operations.
* The module **rest** expose the URI to send the values.
    * This module expose 5 paths to common users:
         * GET ${baseURl}:8091/api/v1/rest/send?message=Hello%20World    
         * GET ${baseURl}:8091/api/v1/rest/division?a=80.30&b=20
         * GET ${baseURl}:8091/api/v1/rest/multiply?a=80&b=20
         * GET ${baseURl}:8091/api/v1/rest/subtract?a=80&b=20
         * GET ${baseURl}:8091/api/v1/rest/sum?a=80&b=20  
  
#Getting Start
* In the directory calculator-wit, run the following command lines:
    * `./gradlew clean build` (To build all modules)
    * `java -jar calculator/build/libs/calculator-0.0.1-SNAPSHOT.jar` (**To run calculator module**)
    * `java -jar rest/build/libs/rest-0.0.1-SNAPSHOT.jar` (**To run rest module**)

###RABBITMQ Docker container configuration
    docker run -d -p 5672:5672 -p 15672:15672 --name my-rabbit rabbitmq:3-management
* Default user and password is **guest / guest**
* Message queues names/types: _calculator_msg_queue_ (**queue**), _calculator_reply_msg_queue_ (**queue**), _calculator_rpc_exchange_ (**exchange/direct**)
