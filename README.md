# 🐶 EdadCanina · v3 (MVC + DataBinding)

Aplicación Android (Java) que calcula la edad "humana" de un perro multiplicando su edad por 7.

Esta es la **versión 3** (rama `v3-mvc`): parte de la v2 (que ya valida la entrada) y reorganiza el código con el patrón **Modelo-Vista-Controlador**, enlazando la pantalla con **DataBinding**. La app hace lo mismo que antes, pero `MainActivity` ya no contiene la lógica.

---

## Objetivos

Al terminar sabrás:

- Activar DataBinding y dejar de usar `findViewById`.
- Separar la lógica en un **Modelo** que no depende de Android y se puede probar con JUnit.
- Hacer un modelo **observable** (`BaseObservable`, `@Bindable`, `notifyPropertyChanged`).
- Enlazar la vista con el modelo con `@{...}` (un sentido) y `@={...}` (dos sentidos).
- Crear un `@BindingAdapter` propio para mostrar los errores desde el XML.
- Crear un **Controlador** sin referencias a vistas y enlazar los clics desde el XML.
- Conservar el estado al girar la pantalla con `onSaveInstanceState`.
- Trabajar con un commit y una etiqueta por cada paso.

---

## Estructura del proyecto

```
es.davante.edadcanina
├── MainActivity.java
├── modelo
│   ├── EdadCaninaModelo.java
│   └── Validacion.java
├── vista
│   └── AdaptadoresVista.java
└── controlador
    └── EdadCaninaControlador.java
```

| Pieza | Clase | Qué hace |
|---|---|---|
| Modelo | `EdadCaninaModelo`, `Validacion` | Guarda los datos, valida y calcula. No importa nada de `android.*` (solo `androidx.databinding`) ni contiene textos para el usuario. |
| Vista | `activity_main.xml`, `AdaptadoresVista` | Muestra el modelo con `@{}` y `@={}`. El adaptador traduce cada `Validacion` a su texto. |
| Controlador | `EdadCaninaControlador` | Recibe las acciones del usuario (`calcular()` y `limpiar()`) y se las pasa al modelo. No conoce ninguna vista. |
| Ensamblador | `MainActivity` | Solo crea y conecta las piezas, y guarda y restaura el estado. |

El `applicationId` es `es.davante.edadcanina.mvc`, distinto del de la v2, para que las dos versiones convivan en el emulador. El `namespace` no cambia.

---

## Paso 0 · Prepara la rama

```bash
git checkout v2-validacion
git checkout -b v3-mvc
```

## Paso 1 · Activar DataBinding

En `app/build.gradle.kts` se cambia el `applicationId` y se añade, dentro de `android { }`:

```kotlin
buildFeatures {
    dataBinding = true
}
```

```bash
git add -A
git commit -m "Paso 1: activar DataBinding"
git tag paso-1
```

## Paso 2 · Layout en `<layout>` y fuera `findViewById`

- Los `xmlns` suben a la etiqueta raíz `<layout>` y el `LinearLayoutCompat` queda dentro.
- `MainActivity` usa `DataBindingUtil.setContentView(...)` y accede a las vistas con `binding.ageInput`, `binding.resultText` y `binding.btnCalc`.

```bash
git add -A
git commit -m "Paso 2: layout con <layout> y Activity con binding, sin findViewById"
git tag paso-2
```

## Paso 3 · Modelo y pruebas JUnit

- `Validacion` es un enum (`OK`, `VACIO`, `NO_NUMERO`, `FUERA_DE_RANGO`).
- `EdadCaninaModelo` guarda la entrada, el resultado y el error, y `calcular()` valida y calcula.
- Los setters del resultado y del error son privados: solo el modelo decide cuándo hay resultado o error.
- Pruebas en `app/src/test`, en `EdadCaninaModeloTest`.

```bash
git add -A
git commit -m "Paso 3: modelo EdadCaninaModelo y enum Validacion con pruebas JUnit"
git tag paso-3
```

## Paso 4 · Modelo observable y enlaces `@{}` y `@={}`

- El modelo extiende `BaseObservable`, los getters llevan `@Bindable` y los cambios se avisan con `notifyPropertyChanged`, solo cuando el valor cambia de verdad.
- El `EditText` usa `@={modelo.entrada}` y el resultado usa `@{}` para el texto con formato y la visibilidad.
- Cuando no hay resultado se usa `INVISIBLE` y no `GONE`, para que el hueco se mantenga y la pantalla no salte.

```bash
git add -A
git commit -m "Paso 4: modelo observable y enlace de la vista con @{} y @={}"
git tag paso-4
```

## Paso 5 · Errores desde el XML con `@BindingAdapter`

- `AdaptadoresVista` (paquete `vista`) define `app:errorValidacion`, que recibe un `Validacion` y muestra el mensaje correspondiente con `setError`.
- El `EditText` solo lleva `app:errorValidacion="@{modelo.error}"`. La Activity ya no llama a `setError` ni a `setText`.

```bash
git add -A
git commit -m "Paso 5: errores mostrados desde el XML con un BindingAdapter propio"
git tag paso-5
```

## Paso 6 · Controlador y botón Limpiar

- `EdadCaninaControlador` tiene un método por acción: `calcular()` y `limpiar()`. No tiene referencias a vistas.
- Los clics se enlazan desde el XML con `android:onClick="@{() -> controlador.calcular()}"`.
- `limpiar()` del modelo deja entrada, resultado y error como al principio.
- Pruebas del controlador en `EdadCaninaControladorTest`.

```bash
git add -A
git commit -m "Paso 6: controlador EdadCaninaControlador, botón Limpiar y clics enlazados desde el XML"
git tag paso-6
```

## Paso 7 · Conservar el estado al girar la pantalla

- `onSaveInstanceState` guarda lo mínimo con claves constantes: la entrada (`KEY_ENTRADA`) y si ya se había calculado (`KEY_CALCULADO`).
- El resultado y el error no se guardan: se vuelven a obtener llamando a `calcular()`.
- La restauración se hace **antes** de enlazar el modelo con la vista.

```bash
git add -A
git commit -m "Paso 7: conservar el estado al girar con onSaveInstanceState"
git tag paso-7
```

---

## Pruebas

Las pruebas están en `app/src/test` y se ejecutan con `./gradlew testDebugUnitTest` o desde Android Studio (clic derecho sobre la carpeta de tests › *Run Tests*).

| Clase | Qué comprueba |
|---|---|
| `EdadCaninaModeloTest` | Vacío, solo espacios, texto, número enorme, 0, 31, negativo, 6 (da 42), límites 1 y 30, error que borra el resultado y entrada que hace caducar el resultado. |
| `EdadCaninaModeloObservableTest` | El modelo avisa al cambiar la entrada, no avisa si el valor es el mismo y avisa al calcular. |
| `EdadCaninaControladorTest` | `calcular()` con entrada válida e inválida, y `limpiar()` (también con la entrada ya vacía). |

---

## Reto realizado

[Escribe aquí el nombre del reto que has elegido de la guía y explica brevemente qué has hecho y cómo lo has probado.]

---

## Explícalo con tus palabras

**¿Qué diferencia hay entre `@{}` y `@={}`?**

`@{}` es un enlace de un solo sentido: cuando cambia el modelo, se actualiza la vista. `@={}` es bidireccional: además, lo que escribe el usuario llega solo al modelo, sin tener que leer el campo con `getText()`.

**¿Por qué `setEntrada` comprueba si el valor ha cambiado antes de avisar?**

Con `@={}` el modelo avisa a la vista y la vista avisa al modelo. Si no se comprueba que el valor sea distinto, se avisarían el uno al otro sin parar y se produciría un bucle infinito.

**¿Por qué el modelo devuelve un enum `Validacion` y no un texto?**

Los textos pertenecen a la interfaz (idioma, formato) y están en `strings.xml`. El modelo solo dice qué ha fallado y es la vista la que decide qué mensaje mostrar. Así el modelo no depende de Android y se puede probar con JUnit sin emulador.

**¿Por qué los setters del resultado y del error son privados?**

Porque solo el modelo sabe cuándo hay un resultado o un error. Si fueran públicos, cualquier clase podría dejar el modelo en un estado incoherente, por ejemplo con un error y un resultado a la vez.

**¿Por qué el resultado caduca cuando cambia la entrada?**

Porque el resultado solo corresponde al número con el que se calculó. Si el usuario cambia el número, mostrar el resultado anterior sería engañoso.

**¿Por qué el controlador no tiene referencias a vistas?**

Porque su trabajo es recibir la acción del usuario y pasársela al modelo. Si tuviera vistas, haría el trabajo de la vista y no se podría probar sin Android. Como solo conoce al modelo, se prueba con JUnit.

**¿Por qué `limpiar()` no se queda en `setEntrada("")`?**

Porque si la entrada ya estaba vacía (con el error de "campo vacío" en pantalla), `setEntrada("")` no cambia nada y el error se quedaría. Por eso `limpiar()` también pone el resultado y el error a su valor inicial.

**¿Por qué se guarda solo la entrada y si ya se calculó, y no el resultado?**

Porque el resultado y el error se pueden volver a obtener a partir de la entrada llamando a `calcular()`. Guardar solo lo mínimo evita tener datos duplicados que se puedan desincronizar.

**¿Por qué se restaura el estado antes de enlazar el modelo con la vista?**

Para que la pantalla nazca ya con los datos correctos. Si se enlazara antes, se vería un instante la pantalla vacía y después cambiaría.

**¿Por qué se usa `INVISIBLE` y no `GONE` para ocultar el resultado?**

Con `GONE` el texto no ocupa espacio y, al aparecer el resultado, el botón se desplazaría hacia abajo. Con `INVISIBLE` el hueco se mantiene y la pantalla no salta.

[Añade aquí las demás preguntas de «Explícalo con tus palabras» que aparezcan en la guía.]

---

## Autoevaluación

[Copia aquí la autoevaluación de la guía y complétala.]

---

## ✅ Lista de comprobación

- [ ] El proyecto compila y la app funciona: vacío, texto, 0 y 31 muestran su error; 6 muestra 42; Limpiar borra todo.
- [ ] No queda ningún `findViewById`.
- [ ] `MainActivity` solo crea y conecta las piezas y guarda el estado.
- [ ] El modelo no importa nada de `android.*` (salvo `androidx.databinding`) ni contiene textos para el usuario.
- [ ] Hay al menos 5 pruebas JUnit en `app/src/test`, todas en verde.
- [ ] Hay un commit por paso (`Paso N: …`) con su etiqueta `paso-1` … `paso-7`.
- [ ] La rama `v3-mvc` y las etiquetas están subidas a GitHub (`git push -u origin v3-mvc --tags`) y el repositorio es público.

---

# 🐶 EdadCanina · v2 (historial)

[Aquí debajo deja el README de la v2 tal como lo tenías.]
