# AI Prompt Log - Stage 2

Model: GPT-5.6 Sol

## Prompt 1

**Prompt:**

¿Me ayudas a revisar cómo debería quedar el constructor de `MemberService` si ahora solo debe depender de `MemberDao`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/service/MemberService.java

---

## Prompt 2

**Prompt:**

Tengo que mover `checkout()` desde `MemberService` a `LoanService`. ¿Qué dependencias debería recibir ahora `LoanService`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/service/LoanService.java

---

## Prompt 3

**Prompt:**

¿Cómo puedo usar `Clock` para reemplazar `LocalDate.now()` y poder fijar la fecha en los tests?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/service/LoanService.java
- src/main/java/cl/ucn/disc/arqsist/library/service/ReservationService.java

---

## Prompt 4

**Prompt:**

¿Me ayudas a revisar cómo envolver en una sola transacción la devolución de un préstamo y el aumento de copias disponibles del libro?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/service/LoanService.java

---

## Prompt 5

**Prompt:**

En `ReservationService`, ¿cómo debería manejar una reserva que no existe y una que ya fue completada?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/service/ReservationService.java

---

## Prompt 6

**Prompt:**

Después de cambiar los constructores de los servicios, `App.java` dejó de compilar. ¿Me ayudas a corregir únicamente el wiring de las dependencias?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/App.java

---

## Prompt 7

**Prompt:**

¿Cómo debería configurar el `Clock` con la zona horaria de Santiago en `App.java`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/App.java

---

## Prompt 8

**Prompt:**

¿Me ayudas a agregar handlers en Javalin para responder 404 con `NotFoundException`, 400 con `IllegalArgumentException` y 409 con `IllegalStateException`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/App.java

---

## Prompt 9

**Prompt:**

¿Cómo elimino la dependencia de `BookDao` desde `BookController` y hago que el listado use solamente `BookService`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/controller/BookController.java

---

## Prompt 10

**Prompt:**

`LoanController` todavía llama a `MemberService.checkout()`. ¿Qué cambio mínimo debo hacer para que use `LoanService.checkout()`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/controller/LoanController.java

---

## Prompt 11

**Prompt:**

¿Es necesario usar `Objects.requireNonNull()` para leer los query parameters en los controllers o puedo parsearlos directamente?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/controller/LoanController.java
- src/main/java/cl/ucn/disc/arqsist/library/controller/ReservationController.java

---

## Prompt 12

**Prompt:**

¿Por qué `DueDateDuplicationTest` necesita un libro con dos copias después de mover el checkout a `LoanService`?

**Files changed:**
- src/test/java/cl/ucn/disc/arqsist/library/DueDateDuplicationTest.java

---

## Prompt 13

**Prompt:**

¿Cómo creo un `Clock.fixed` para que dos servicios usen exactamente la misma fecha durante un test?

**Files changed:**
- src/test/java/cl/ucn/disc/arqsist/library/DueDateDuplicationTest.java
- src/test/java/cl/ucn/disc/arqsist/library/TransactionBugTest.java

---

## Prompt 14

**Prompt:**

En `TransactionBugTest`, ¿qué excepción específica debería comprobar cuando se intenta hacer checkout con un miembro que no existe?

**Files changed:**
- src/test/java/cl/ucn/disc/arqsist/library/TransactionBugTest.java

---

## Prompt 15

**Prompt:**

¿Cómo calculo la cantidad de días vencidos entre `dueDate` y la fecha actual usando `ChronoUnit.DAYS`?

**Files changed:**
- src/main/java/cl/ucn/disc/arqsist/library/service/LoanService.java

---

## Prompt 16

**Prompt:**

¿Me ayudas a comprobar en un test que un préstamo vencido acumule una multa, pero que un préstamo activo mantenga la multa en 0?

**Files changed:**
- src/test/java/cl/ucn/disc/arqsist/library/AccruedFeeTest.java

---

## Prompt 17

**Prompt:**

¿Cómo puedo comprobar que `seedIfEmpty()` es idempotente y no vuelve a crear préstamos ni reservas al llamarlo dos veces?

**Files changed:**
- src/test/java/cl/ucn/disc/arqsist/library/DatabaseSeedTest.java

---

## Prompt 18

**Prompt:**

¿Me ayudas a revisar qué dependencias deberían aparecer en el diagrama de clases después del refactor del Stage 2?

**Files changed:**
- docs/class-diagram.puml