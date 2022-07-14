# Install java 11

- <a href="https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-22-04">How To Install Java with Apt on Ubuntu 22.04</a>

- Install Java
```bash
sudo apt update
sudo apt install openjdk-11-jre
```
- Switch java version
```bash
sudo update-alternatives --config javac
sudo update-alternatives --config java
```

- Check java version
```bash
java -version
javac -version
```

- Install sdkman
```bash
curl -s "https://get.sdkman.io" | bash
```

# Install Maven

- Install via apt
```bash
sudo apt update
sudo apt install maven -y
mvn -version
```

- Install via sdkman
```bash
sdk install maven 3.8.6
```

- Swicth maven version installed via apt
```bash
sudo update-alternatives --config mvn
```

- Swicth maven ersion using sdkman
```bash
sdk default maven 8.6.3
sdk use maven 8.6.3
```

- Check maven version
```bash
mvn --version
```