# Notre fonctionnement
## connexion
le client demande a l'utilisateur de rentrer son pseudo
premier message envoyé par le client au serveur pour se connecter
```
/co <pseudo>
```

## choix du salon
message envoyé par le serveur pour demander au client de choisir un salon en reponse à la connexion
```
choix du salon(avec /join <salon>) : <salon1> <salon2> ...
```

message envoyé par le client pour choisir un salon
```
/join <salon>
```


## deconexion
message envoyé par le client pour se deconnecter
```
/quit
```

# données stockées

```
Pseudo -> port / adresse ip
Salon -> liste de pseudo
```