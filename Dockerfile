# Définir l'image de base avec la version de Java et le système d'exploitation Linux
FROM openjdk:17-jdk-alpine
RUN apk update

# Définir un répertoire de travail
WORKDIR /cdz-ocr

# Copier les fichiers de configuration et de dépendances du projet
COPY target/spring-boot-docker.jar /cdz-ocr
#COPY C:/Program Files/Tesseract-OCR/tessdata /cdz-ocr/tessdata/

# Installer Tesseract OCR
RUN apk add --update tesseract-ocr

# Définir les variables d'environnement pour Tesseract OCR
ENV LC_ALL=C
ENV TESSDATA_PREFIX=/usr/share/tessdata/

# Copier votre application dans l'image Docker
COPY . /cdz-ocr


# Lancer l'application Spring Boot lors du démarrage du conteneur
CMD ["java", "-jar", "spring-boot-docker.jar"]

