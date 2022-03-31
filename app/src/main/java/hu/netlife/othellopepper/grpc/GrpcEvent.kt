package hu.netlife.othellopepper.grpc

import hu.netlife.othellopepper.proto.OthelloPepper

data class GrpcEvent(
    val messageType: OthelloPepper.MessageType,
    val playerId: Int
)