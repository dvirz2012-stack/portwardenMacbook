package com.dvir.portwarden.services

import com.dvir.portwarden.model.PortInfo

class PortScanner {

    fun scanListeningPorts(): List<PortInfo>{

        val results = mutableListOf<PortInfo>()
        val lsofProcess = ProcessBuilder("lsof", "-n", "-P", "-iTCP", "-sTCP:LISTEN", "+c", "0").start()

        lsofProcess.inputStream.bufferedReader().useLines {lines ->

            lines.drop(1).forEach {line ->

                val parts = line.trim().split(Regex("\\s+"))
                if (parts.size < 3) return@forEach

                val processName = parts[0].replace("\\x20", " ")
                val pid = parts[1].toLongOrNull() ?: return@forEach

                val address = parts[parts.size - 2]
                val port = address.substringAfterLast(":").toIntOrNull() ?: return@forEach

                results.add(PortInfo(port, pid, processName))

            }

        }

        lsofProcess.waitFor()

        return results.distinct()

    }

    fun isKillable(targetPort: Int, targetPid: Long): Boolean{

        return scanListeningPorts().any {

            it.portNumber == targetPort && it.pid == targetPid

        }

    }


}


}

