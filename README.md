**Inhaltsangabe:**
- Einleitung
- Projektziel
- Technologien
- Versionsverwaltung
- Dokumentation
- Hintergrund

**Einleitung:**
Deine örtliche Stadtbibliothek benötigt ein neues Verwaltungssystem für ihre Bücher. Da die Bibliothek keine großen finanziellen Mittel zur Verfügung hat und sie dir sehr am Herzen liegt, hast du dich freiwillig bereit erklärt, das System zu entwickeln. Zusammen mit deinem besten Freund machst du dich nun an die Planung. Ihr habt euch darauf geeinigt, dass du dich um das Backend kümmerst während er das Frontend entwickelt. 

**Projektziel:**
Das Ziel des Projekts ist es ein Backend zu erstellen, welches mithilfe einer REST-Schnittstelle in der Lage ist Informationen in einer Datenbank abzuspeichern, zu löschen und zu manipulieren.

**Zu speichernde Informationen:**

|name|title|author|publicationDate|pageNumber|id|
|----|-----|------|---------------|----------|--|
|**type**|String|String|String (dd.mm.yyyy)|String|string|


**Folgende Funktionalitäten sollen implementiert werden:**
- Es soll ein Buch angelegt werden können (doppelte Bücher können vorkommen) 
- Es kann ein Buch anhand des Titels abgefragt werden 
- Es können alle Bücher eines Autors abgefragt werden 
- Es können alle Bücher, die dasselbe Erscheinungsjahr haben abgefragt werden 
- Es können alle Bücher, die dasselbe Genre haben abgefragt werden 
- Es soll möglich sein, die aktuelle Anzahl aller existierenden Bücher abzufragen

**Zum entwicklen des Backends wird folgendes verwendet:**
- Spring Boot 
- Java 8 
- Maven 
- MongoDB

**Folgende Mittel werden für die Dokumentation genutzt:**
- SwaggerUI
- Confluence
- Für das Management der Aufgaben wird Trello verwendet. 
  
**Hintergrund:**
Diese Aufgabenstellung ist im Rahmen einer Azubi-betreuung entstanden,
um Auszubildende in Projektnahe-Technologien zu unterweisen. 


Autor: Vincenzo Aurricchio
