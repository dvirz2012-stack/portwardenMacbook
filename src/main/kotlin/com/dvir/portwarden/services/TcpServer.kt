package com.dvir.portwarden.services

import com.dvir.portwrden.model.Commands

import java.net.ServerSocket

import java.io.bufferedInputStream

class TcpServer {

    fun startServer(serverPort: Int){

        val myServer = ServerSocket(serverPort)
        val commandPortScanner = PortScanner()
        val killProcess = ProcessManager()
        val client = myServer.accept()

        try {

            while (true){

                val ports = commandPortScanner.scanListeningPorts()
                val clientReader = client.getInputStream.bufferedReader()
                val clientWriter = java.io.PrintWriter(client.getOutputStream(), true)
                val clientMessage = clientReader.readLine().trim().uppercase()
                clientWriter.println("Client says: $clientMessage")
                val command = Commands.valueOf(clientMessage)

                when (command) {

                    Commands.FETCH_PORTS -> {

                        ports.forEach {portInfo -> clientWriter.println("Port: ${portInfo.port} | PID: ${portInfo.pid}")}

                    }

                    Commands.KILL_PROCESS -> {

                        clientWriter.println("Which port?: ")
                        val portInputStr = ClientReader.readLine()
                        val portInput: Int? = portInputStr?.toIntOrNull()
                        var targetPid: Long = -1L
                        ports.forEach {portInfo ->
                            if (portInfo.port == portInput){

                                targetPid = portInfo.pid

                            }
                        }

                        if (portInfo == null || targetPid == -1L){

                            clientWriter.println("No process listening on that port")

                        } else if (!commandPortScanner.isKillable(portInput, targetPid)){
"
                            ClientWriter.println("Port owner changed, try again.")

                        } else if (!killProcess.killProcess(targetPid)){

                            ClientWriter.println("Couldn't kill process, permission denied")

                        } else {

                            ClientWriter.println("Process $targetPid killed successfully")

                        }

                    }


                }

            }

        } catch (e: Exception){

            println("You've written a wrong command.")

        }

    }

}