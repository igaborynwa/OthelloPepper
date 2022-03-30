package hu.netlife.othellopepper.grpc

data class GrpcEvent(
    val messageId: String,
    val messageType: String,
    val message: String
)