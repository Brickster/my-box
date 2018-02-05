# my-box

Your box. Your stuff.<sup>[1](#myfootnote1)</sup>

[![Build Status](https://travis-ci.org/Brickster/my-box.svg?branch=master)](https://travis-ci.org/Brickster/my-box) [![Docker Pulls](https://img.shields.io/docker/pulls/brickster/my-box.svg)](https://hub.docker.com/r/brickster/my-box/)

### Getting Started

myBox is fairly straightforward. It uses Groovy, Spring Boot, MongoDB, and Swagger UI. Just to name a few. However, you don't need to worry about any of that. Simply install [Docker](https://docs.docker.com/engine/installation/) and run the following from the repository directory:

```
docker-compose -f src/main/resources/compose/compose.yml up -d
```

Navigate to [http://localhost:8080/my-box/v1/documents]() and you'll find you have your own myBox filled with nothing. _Awesome!_

To start putting stuff in your box, navigate to [http://localhost:8090]() and check the documentation molded from handcrafted Swagger YAML. _Nice!<sup>[2](#myfootnote3)</sup>_

### Getting Ended<sup>[3](#myfootnote3)</sup>

To tear it all down, run:

```
docker-compose -f src/main/resources/compose/compose.yml down
```

---

<a name="myfootnote1">1</a>: Until you kill the application, Docker, Earth, etc. Then your stuff is gone. Sorry.</br>
<a name="myfootnote2">2</a>: Nice!</br>
<a name="myfootnote3">3</a>: That sounded better in my head.
