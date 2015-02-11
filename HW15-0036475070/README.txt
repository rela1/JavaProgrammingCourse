Za uspješno pokretanje zadaće pretpostavlja se da je kreirana baza podataka votingDB s 
relacijama Polls i PollOptions. Relacije ne moraju biti popunjene jer se to automatski
obavlja preko servleta /init.

Defaultna vrijednost za username je "ivica", a za password "ivo". 

Navedene stavke se mogu promijeniti na željene ako je to različito postavljeno na 
Vašem računalu u fileu properties/database.properties. 

Za uspješno izvođenje programa također je pretpostavljeno da je upravitelj 
bazom podataka pokrenut i da mu se može pristupiti 
preko adrese "jdbc:derby://localhost:1527/votingDB".