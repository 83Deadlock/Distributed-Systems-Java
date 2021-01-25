# COVID-Warning-System

This project was designed to be a system that allows users to register and log into their account.
Users can either be clients (credential == 0) or health workers (credential != 0).
The system has a Server-Client connection allowing multiple Client connections to the same server.
Users can change their position on a map, see how many people are at a certain location at each moment, ask to be warned if a position becomes free (#people == 0) and notify the system that the user is sick.
Once the user is sick, it gets locked out of the system.

Health Workers can also see the full map, and on each location get information on how many people got sick out of all people who were in that same location at least once.

If a user gets sick, everyone who crossed paths with this user will get a notification that they may be infected.
