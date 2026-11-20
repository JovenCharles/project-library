# AI Prompt Log — Stage 3

Model used: **GPT-5.6 Sol**

## Prompt 1

**Prompt**

> ¿Es normal que se vean esas líneas amarillas en el build.gradle después de agregar gRPC y protobuf?

**Files changed**

- Ninguno.

---

## Prompt 2

**Prompt**

> ¿Está bien crear `member.proto` dentro de `src/main/proto`?

**Files changed**

- `src/main/proto/member.proto`

---

## Prompt 3

**Prompt**

> Ya generé las clases protobuf. ¿Qué debería agregar a `MemberService` para poder obtener un miembro por id?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/service/MemberService.java`

---

## Prompt 4

**Prompt**

> Creé el package `grpc`. ¿Está bien crear ahí `MemberGrpcService.java`?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/grpc/MemberGrpcService.java`

---

## Prompt 5

**Prompt**

> El proyecto compila, pero IntelliJ me mostraba varias líneas rojas en `MemberGrpcService`. ¿Puede ser porque todavía no había refrescado Gradle?

**Files changed**

- Ninguno.

---

## Prompt 6

**Prompt**

> ¿Cómo conecto el servidor gRPC al `App.java` para que se levante junto con Javalin?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/App.java`

---

## Prompt 7

**Prompt**

> ¿Dónde debería crear `MemberClient.java`?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/grpc/MemberClient.java`

---

## Prompt 8

**Prompt**

> ¿Dónde creo una clase para ejecutar el cliente gRPC desde Gradle?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/MemberClientApp.java`
- `build.gradle`

---

## Prompt 9

**Prompt**

> ¿Por qué `gradlew run` se queda en 88% aunque Javalin y gRPC ya iniciaron?

**Files changed**

- Ninguno.

---

## Prompt 10

**Prompt**

> ¿Cómo creo una segunda terminal en IntelliJ para ejecutar `runClient` mientras el servidor sigue corriendo?

**Files changed**

- Ninguno.

---

## Prompt 11

**Prompt**

> ¿Dónde debería crear `MemberGrpcTest.java`?

**Files changed**

- `src/test/java/cl/ucn/disc/arqsist/library/MemberGrpcTest.java`

---

## Prompt 12

**Prompt**

> Quiero probar el recorrido completo de gRPC sin abrir un puerto real. ¿Cómo puedo hacerlo con un servidor in-process?

**Files changed**

- `src/test/java/cl/ucn/disc/arqsist/library/MemberGrpcTest.java`

---

## Prompt 13

**Prompt**

> `runClient` me da `Connection refused` en `127.0.0.1:50051`. ¿Qué significa?

**Files changed**

- Ninguno.

---

## Prompt 14

**Prompt**

> ¿Qué debería documentar en el README sobre la API gRPC y cómo ejecutar el cliente?

**Files changed**

- `README.md`

---

## Prompt 15

**Prompt**

> Ayúdame a actualizar el diagrama de clases para agregar el package gRPC, el cliente, el servicio y las clases generadas.

**Files changed**

- `docs/class-diagram.puml`

---

## Prompt 16

**Prompt**

> Para el ejercicio de búsqueda de miembros, ¿qué tengo que agregar al archivo `member.proto`?

**Files changed**

- `src/main/proto/member.proto`

---

## Prompt 17

**Prompt**

> ¿Cómo implemento en `MemberDao` una búsqueda por substring que ignore mayúsculas y minúsculas y trate `%` y `_` como caracteres normales?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/dao/MemberDao.java`

---

## Prompt 18

**Prompt**

> ¿Qué método agrego a `MemberService` para validar la búsqueda y quitar espacios al inicio y al final?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/service/MemberService.java`

---

## Prompt 19

**Prompt**

> ¿Cómo agrego el RPC `searchMembers` en `MemberGrpcService` usando el método de búsqueda del servicio?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/grpc/MemberGrpcService.java`

---

## Prompt 20

**Prompt**

> ¿Cómo agrego `search(String query)` a `MemberClient` para llamar al nuevo RPC?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/grpc/MemberClient.java`

---

## Prompt 21

**Prompt**

> ¿Dónde creo `MemberSearchTest.java` y qué casos debería probar?

**Files changed**

- `src/test/java/cl/ucn/disc/arqsist/library/MemberSearchTest.java`

---

## Prompt 22

**Prompt**

> `MemberSearchTest` falla con `IllegalArgumentException` en la primera búsqueda `"ada"`. ¿Qué está mal?

**Files changed**

- `src/main/java/cl/ucn/disc/arqsist/library/dao/MemberDao.java`

---

## Prompt 23

**Prompt**

> El test de búsqueda ya pasa. ¿Qué me falta revisar para cerrar el Stage 3?

**Files changed**

- Ninguno.
