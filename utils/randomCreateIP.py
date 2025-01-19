import random
import socket
import struct

with open('D:/A_Study/coding/ANTS/bdd-undergra-thinking/src/main/java/org/ants/ips', 'a') as f:
    i = 0
    while i < 1000:
        ip = socket.inet_ntoa(struct.pack('>I', random.randint(1, 0xffffffff)))
        mask = random.randint(10, 32)
        ip_with_mask = ip + '/' + str(mask) + '\n'
        f.write(ip_with_mask)
        i += 1
