### Code Breakdown

```java
http.authorizeRequests(authorize -> authorize.anyRequest().authenticated())
```

> Means every damn request must be authenticated. No public endpoints. Everyone’s getting carded at the door.

---

```java
.csrf(csrf -> csrf.disable())
```

> CSRF protection is turned off because you’re doing stateless authentication (JWT).
> CSRF is mainly for forms with cookies — and you’re not using those. So yeah, safe to disable.

---

```java
.sessionManagement(
    session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

> No session is created. The server doesn’t remember who you are between requests — like a goldfish.
> This is perfect for token-based (JWT) auth.

---

```java
.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
```

> Tells Spring that this backend is a **resource server** that will verify JWT tokens (from Keycloak or any other issuer).
> It automatically checks the `Authorization: Bearer <token>` header for you.

---

```java
.addFilterAfter(userProvisioningFilter, BearerTokenAuthenticationFilter.class);
```

> Adds your custom `UserProvisioningFilter` right after the built-in JWT verification.
> So it runs **after** the token is validated — perfect place to sync or create user data.
