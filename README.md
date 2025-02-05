# PortConnectionTest

This is a simple command line application for testing communication over network.
I created this for testing communication between remote client application and server hidden behind NAT accessible via port forwarding.

---
## Minimal requirements
JRE 8

---

Start server with TCP or UDP ports openned and then start client and check connection.

## Example

Start server listening on ports 1001,1002,1003,1004,1005 and 1020
`java -jar pct.jar listen tcp 1001-1005,1020`

Connect client same way
`java -jar pct.jar connect 127.0.0.1 tcp 1001-1005,1020`

Call `java -jar pct.jar help` to get help printed

