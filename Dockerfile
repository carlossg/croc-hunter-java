FROM registry.fedoraproject.org/fedora-minimal
WORKDIR /work/
COPY target/*-runner /work/application
RUN chmod 775 /work
# we are compiling with different dependencies, let's make the binary work with a hack
RUN ln -s /lib64/libcrypt.so.2 /lib64/libcrypt.so.1
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
