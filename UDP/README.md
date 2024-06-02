# Notre fonctionnement
## message traité par le serveur

```	
/<commande> <parametre>
```
commande possible vers le serveur:
- /co : connexion
- /quit : deconnexion
- /room : choix du salon
- /msg : message

commande possible vers le client:
- /co : retour de connexion avec le port de connexion
- /err : erreur a afficher
- /info : information a afficher

## connexion
le client demande a l'utilisateur de rentrer son pseudo et le port sur lequel est le serveur.
premier message envoyé par le client au serveur pour se connecter ```/co <pseudo>```.
Sans ce message le serveur ne traitera pas les autres messages car l'utilisateur n'est pas enrégistré dans la liste des clients connectés.

Le Pseudo doit être en un seul mot sans espace et ne doit pas être déjà utilisé par un autre client sinon le serv renvoie un message d'erreur```/err pseudo déjà utilisé ```.

sinon le serveur enregistre le client dans la liste des clients connectés et lui envoie le nouveau port sur lequel il devra envoyer ses messages ```/co <port>```.

## deconexion

Message envoyé par le client pour se deconnecter ```/quit```.
le serveur supprime le pseudo de la liste des clients connectés et le client ne peut plus envoyer de message au serveur tant qu'il ne s'est pas reconnecté avec ```/co <pseudo>``` sur le port de connexion du serveur.

## choix du salon
message envoyé par le serveur pour demander au client de choisir un salon en reponse à la connexion
```
choix du salon(avec /room <salon>) : <salon1> <salon2> ...
```

message envoyé par le client pour choisir un salon
```
/room <salon>
```

# données stockées

```
Pseudo
port
adresse ip
Salon
est connecté
```