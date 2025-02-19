import random
import socket
import struct

with open('src/main/resources/org/ants/ips', 'a') as f:
    i = 0
    while i < 1000:
        ip = socket.inet_ntoa(struct.pack('>I', random.randint(1, 0xffffffff)))
        mask = random.randint(1, 32)
        ip_with_mask = ip + '/' + str(mask) + '\n'
        f.write(ip_with_mask)
        i += 1
