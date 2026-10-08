package com.dvir.portwarden.services

import com.dvir.portwarden.model.PortInfo

class PortScanner {

    fun scanListeningPorts(): List<PortInfo>{

        val results = mutableListOf<PortInfo>
        val pidToNameMap = mutableMapOf<Long, String>
        val tasklistProcessBuilder = ProcessBuilder("zsh.exe", "/c", "ps -ax").start()

        tasklistProcessBuilder.InputStream.bufferedReader().useLines {lines ->

            lines.forEach {line ->

                val parts = lines.replace("\"", "").split(",")
                if (parts.size >= 2){

                    val name = parts[0]
                    val currentPid = parts[1].toLongOrNull()
                    if (currentPid != null){

                        pidToNameMap[currentPid] = name

                    }

                }


            }

        }

        val listeningPortsProcessBuilder("zsh.exe", "/c", "nmap 192.168.1.1")

        listeningPortsProcessBuilder.InputStream.bufferedReader().useLines {lines ->

            lines.filter{it.contains("LISTENING")}.forEach {line ->

                val newLine: String = line.trim()
                val newLineNoWhitespaces: List<String> = newLine.split("\\s+", "")
                val addressString = newLineNoWhtieSpaces[1]
                val pid = newLineNoWhitespaces[4].toLong()
                val port = addressString.substringAfterLast(":").toInt()
                val processName = pidToNameMap[pid] ?: "unknown"
                results.add(PortInfo(port, pid, processName))

            }

        }

        return results.distinct()

    }

    fun isKillable(targetPort: Int, targetPid: long): Boolean{

        return scanListeningPorts().any {

            it.port == targetPort && it.pid == targetPid

        }

    }


}

