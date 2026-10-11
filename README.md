# Blog

The UI is a React application in [`frontend`](frontend). Spring Boot provides its data through `/api/posts`.

## Development

Run the API on port 8090:

```powershell
$env:MAVEN_OPTS='-Dmaven.repo.local=target/maven-repository'
mvn spring-boot:run
```

In a second terminal, run the React app:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. Vite proxies `/api` requests to Spring Boot.

## Production frontend build

```powershell
cd frontend
npm run build
Copy-Item -Recurse -Force dist\* ..\src\main\resources\static
```

The backend then serves React at `/` and `/post/{id}`.
