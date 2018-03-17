CC=gcc
LIBSOCKET=-lnsl
CCFLAGS=-Wall -g
SRV=server
CLT=client
PORT = 10000
IP_SERVER = "127.0.0.1"

all: $(SRV) $(CLT)

build: $(SRV) $(CLT)

$(SRV):$(SRV).c
	$(CC) $(CCFLAGS) -o $(SRV) $(LIBSOCKET) $(SRV).c

$(CLT):	$(CLT).c
	$(CC) $(CCFLAGS) -o $(CLT) $(LIBSOCKET) $(CLT).c

clean:
	rm -f $(SRV) $(CLT)

# Ruleaza serverul
run_server:
	./$(SRV) ${PORT}

run_client:
	./$(CLT) ${IP_SERVER} ${PORT}