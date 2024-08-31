# Verwenden eines leichteren Basisbildes
FROM node:18-alpine as build-stage

# Arbeitsverzeichnis im Container festlegen
WORKDIR /app

# Abhängigkeiten installieren
# Kopieren nur der package.json und package-lock.json zuerst
COPY package*.json ./
RUN npm install

# App-Quellcode kopieren
COPY . .

# Build der App erstellen
RUN npm run build

# Production Stage
# Verwendung eines leichteren Basisbildes für die Produktionsumgebung
FROM nginx:alpine

# Nginx so konfigurieren, dass die statischen Dateien bedient werden
COPY --from=build-stage /app/build /usr/share/nginx/html

# Standard Nginx-Port freigeben
EXPOSE 80

# Standardbefehl von Nginx verwenden (Start des Servers)
CMD ["nginx", "-g", "daemon off;"]

#befehl zum erstellen von image 
#docker buildx build --platform linux/amd64,linux/arm64 -t andilosing/frontend-service:v8 --push .
#andi@MacBook-Air-von-Andi portfolio-service % docker buildx ls
#
#NAME/NODE           DRIVER/ENDPOINT     STATUS    BUILDKIT   PLATFORMS
#mybuilder*          docker-container                         
# \_ mybuilder0       \_ desktop-linux   running   v0.14.1    linux/arm64, linux/amd64, linux/amd64/v2, linux/riscv64, linux/ppc64le, linux/s390x, linux/386, linux/mips64le, linux/mips64, linux/arm/v7, linux/arm/v6
#default             docker                                   
# \_ default          \_ default         running   v0.13.2    linux/arm64, linux/amd64, linux/amd64/v2, linux/riscv64, linux/ppc64le, linux/s390x, linux/386, linux/mips64le, linux/mips64, linux/arm/v7, linux/arm/v6
#desktop-linux       docker                                   
# \_ desktop-linux    \_ desktop-linux   running   v0.13.2    linux/arm64, linux/amd64, linux/amd64/v2, linux/riscv64, linux/ppc64le, linux/s390x, linux/386, linux/mips64le, linux/mips64, linux/arm/v7, linux/arm/v6


