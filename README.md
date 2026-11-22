<div align="center">

# Portwarden for Mac: Backend

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![macOS](https://img.shields.io/badge/macOS-000000?style=for-the-badge&logo=apple&logoColor=white)
![Status](https://img.shields.io/badge/status-in%20development-orange?style=for-the-badge)

**Hi, this is the Mac version of Portwarden! Same idea, now on a MacBook :)**

</div>

## What Is Portwarden?

Portwarden is a web application that shows you every port that is listening on your computer, together with its PID and the name of the process using it. If something is hogging a port you need, you just click **Kill** and it's gone. No more hunting through the terminal to find out who is sitting on port 3000 :)

The original Portwarden was built for Windows, and it's ready to use right now: [dvirz2012-stack/portwarden](https://github.com/dvirz2012-stack/portwarden). This repo is where Portwarden learns to speak macOS.

## Status

The Mac version is still being built, so it doesn't run yet. Here's where things stand:

| Part | What it does | Status |
|------|--------------|--------|
| Models | `PortInfo`, `Commands` and `JsonResponse`, the shapes of the data | Done |
| Process manager | Kills a process by its PID, safely | In progress |
| Port scanner | Finds every listening port and the process behind it | In progress |
| Server | Talks to the frontend and handles its commands | In progress |
| Application | Starts everything up | Coming soon |

The biggest job here is the port scanner. Windows and macOS find listening ports with completely different tools, so this part is being rewritten for the Mac. The rest of the design stays the same.

## How It Works

The frontend and the backend are connected by an "invisible wire": a WebSocket running on localhost.

1. The frontend sends a command to the backend, either `FETCH_PORTS` or `KILL_PROCESS`.
2. The backend does the work: it scans the ports, or kills the process you picked.
3. It sends back a JSON response, and the frontend uses it to build the table or remove a port from it.

A response looks like this:

```json
{
  "message": "Ports fetched successfully",
  "data": [{ "port": 3000, "pid": 1234, "processName": "node" }],
  "type": "SUCCESS"
}
```

The `type` can be `SUCCESS`, `ERROR`, `PROMPT` or `INFO`, so the frontend always knows what kind of message just arrived.

## Safety First

Portwarden is designed to be careful about what it kills:

- **It won't kill itself.** If you ask it to kill its own process, it politely refuses. Self-preservation is important :)
- **It double-checks before killing.** Right before stopping a process, it checks that the same process still owns that port. If something changed in the meantime, it asks you to try again instead of killing the wrong thing.
- **It tells you what happened.** If a process can't be killed because of permissions, you get a clear message instead of silence.

## Project Structure

| File | Purpose |
|------|---------|
| `Application.kt` | The starting point of the backend. |
| `model/PortInfo.kt` | One listening port: its number, its PID and its process name. |
| `model/Commands.kt` | The commands the frontend can send. |
| `model/JsonResponse.kt` | The shape of every response, and the response types. |
| `services/PortScanner.kt` | Finds the listening ports and the processes behind them. |
| `services/ProcessManager.kt` | Kills a process by its PID. |
| `services/TcpServer.kt` | A simple server for testing commands. |
| `routes/WebSocketRoutes.kt` | The WebSocket connection to the frontend. |

All files live under `src/main/kotlin/com/dvir/portwarden/`.

## How to Run It

Instructions will be added here as soon as the Mac version is up and running. In the meantime, Windows users can already try the [Windows version](https://github.com/dvirz2012-stack/portwarden) :)

## Roadmap

- Finish the Mac port scanner and server, so Portwarden runs on macOS just like it does on Windows.
- Make Portwarden available to everyone.
- Add strong security features, so it's both secure and comfortable to use.

## Related Repos

- **Windows backend:** [dvirz2012-stack/portwarden](https://github.com/dvirz2012-stack/portwarden)
- **Frontend:** [dvirz2012-stack/portwardenFrontend](https://github.com/dvirz2012-stack/portwardenFrontend)

<div align="center">

made by **Dvir :)**

</div>
