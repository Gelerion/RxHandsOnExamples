Netty is entirely event-driven; we never block waiting for data to be sent or received. Instead, raw bytes in the
form of ByteBuf instances are pushed to our processing pipeline. TCP/IP gives us an impression of connection and
data flowing byte after byte between two computers. But in reality TCP/IP is built on top of IP, which can barely
transfer chunks of data known as packets. It is the operating system’s role to assemble them in the correct order
and give the illusion of a stream. Netty drops this abstraction and works at a byte-sequence layer, not a stream.
Whenever a few bytes arrive to our application, Netty will notify our handler. Whenever we send few bytes, we get a
ChannelFuture without blocking (more on futures in a second).