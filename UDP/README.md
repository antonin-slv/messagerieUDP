# Notre fonctionnement
## message traité par le serveur

```/<commande> <parametre>```

commande possible vers le serveur:
- /co : connexion
- /quit : deconnexion
- /room : choix du salon
- /msg : message
- /hist : historique

commande possible vers le client:
- /co : retour de connexion
- /err : erreur a afficher
- /info : information a afficher
- /msg : affiche le message
- /hist : affiche l'historique

## connexion

le client demande a l'utilisateur de rentrer son pseudo et le port sur lequel est le serveur.
premier message envoyé par le client au serveur pour se connecter ```/co <pseudo>```.
Sans ce message le serveur ne traitera pas les autres messages car l'utilisateur n'est pas enrégistré dans la liste des clients connectés.

Le Pseudo sera automatiquement converti en une seule chaine de caractère en remplaçant les espaces par des underscores.
Le Pseudo ne doit pas être déjà utilisé par un autre client sinon le serv renvoie un message d'erreur```/err pseudo déjà utilisé ```.
Le serveur enregistre le client dans la liste des clients connectés et lui envoie le nouveau port sur lequel il devra envoyer ses messages ```/co <port>```.

Le serveur lance un thread pour chaque client connecté qui va traiter les messages envoyés par le client.
Chacun de ces threads utilisent un port différent pour communiquer avec le client.

## deconexion

Message envoyé par le client pour se deconnecter ```/quit```.
Le serveur passe l'utilisateur a deconecté dans la liste des clients et le thread du serveur dedié a ce client se termine.
Le client ne peut plus envoyer de message au serveur tant qu'il ne s'est pas reconnecté avec ```/co <pseudo>``` sur le port de connexion du serveur.

## choix du salon

Apres la connexion le client est dans le salon par defaut "bienvenue".
Il peut choisir un autre salon en envoyant un message au serveur ```/room <salon>```.
Si le salon n'existe pas, il est créé.

## envoi de message

Lorsque le client envoie un message ```<text>``` il est modifié par l'applicatif client pour qu'il soit envoyé au serveur sous la forme ```/msg <text>```.
Le serveur le reçoit et l'envoie a tous les clients qui sont dans le meme salon que l'envoyeur.

## historique

Le client peut demander l'historique des 10 derniers messages du salon dans lequel il se trouve en envoyant ```/hist```.
Le serveur lui envoie les 10 derniers messages sous la forme ```/hist <date/heure> <pseudo> <message>``` pour chaque message (10 messages maximum).

## notre structure de données

une liste de clients:
- private String pseudo
- private InetAddress ip
- private int port
- private String room
- private boolean connected

un dossier ```rooms``` contenant les salons.
chaque salon est un fichier texte contenant les messages envoyés dans ce salon sous la forme ```<date/heure> <pseudo> <message>```.