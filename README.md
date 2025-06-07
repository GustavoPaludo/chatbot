A simple Spring Boot application that receives a user message and returns the output of an annotated method with AIContext using Langchain4j and the llama3 LLM. 

How to run:

First you'll need to install the ollama on your system and install within it the llama3 model. The download page is: https://ollama.com/download. Once the ollama is installed, you'll need to pull the llama3 model.
You can do it by running the following comand into a prompt terminal: 'ollama pull llama3'. Once finished, run 'ollama run llama3'; It automaticaly runs on the port 11434.

Once it's done you'll just need to run the Spring application and make a request to the endpoint /chat to start the run.
