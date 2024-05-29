# Notre fonctionnement
## message traité par le serveur

```	
/<commande> <parametre>
```
commande possible :
- /co : connexion
- /quit : deconnexion
- /join : choix du salon
- /msg : message

## connexion
le client demande a l'utilisateur de rentrer son pseudo
premier message envoyé par le client au serveur pour se connecter
```
/co <pseudo>
```
sans ce message le serveur ne traitera pas les autres messages car l'utilisateur n'est pas enrégistré dans la liste des clients connectés (il n'a pas de pseudo associé à une adresse ip et un port)

Le Pseudo doit être en un seul mot sans espace et ne doit pas être déjà utilisé par un autre client

## deconexion
message envoyé par le client pour se deconnecter
```
/quit
```
le serveur supprime le pseudo de la liste des clients connectés et le client ne peut plus envoyer de message au serveur tant qu'il ne s'est pas reconnecté avec ```/co <pseudo>```	

## choix du salon
message envoyé par le serveur pour demander au client de choisir un salon en reponse à la connexion
```
choix du salon(avec /join <salon>) : <salon1> <salon2> ...
```

message envoyé par le client pour choisir un salon
```
/join <salon>
```

# données stockées

```
Pseudo
port
adresse ip
Salon
est connecté
```