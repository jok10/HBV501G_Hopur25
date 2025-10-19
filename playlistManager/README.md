# Playlist Manager

Backend Java Spring Boot with JPA and H2 database for development and testing.

The program implements:
- **UC1 - Add Tracks to Playlist**
- **UC2 - Search Tracks**
- **UC3 - Create Playlist**
- **UC4 - Delete Playlist**

The program also creates four sample tracks on startup for testing purposes.

---

Steps to perform in order to be able to verify the use cases (locally):
1) Run in the console:
```bash
./mvnw spring-boot:run
```
2) Open in the browser: http://localhost:8080/h2-console

H2 console login
URL: http://localhost:8080/h2-console  
JDBC URL: jdbc:h2:mem:pmdb  
User: sa  
Password: (none)  

Optional SQL track check:
```sql
SELECT * FROM TRACKS ORDER BY TRACK_ID;
```

---

**Verified Use Cases (UC)**
*(as of 19 Oct 2025)*


## **UC1 - Add Tracks to Playlist**

Adds existing tracks to a playlist.

**Methods and Paths:**
- Create playlist: **POST** `/api/playlists`
- Add track: **POST** `/api/playlists/{playlistId}/tracks?trackId={trackId}`
- List playlist tracks: **GET** `/api/playlists/{playlistId}/tracks`



## **UC2 - Search Tracks**

Searching returns matching tracks in the paginated form. Case-insensitive search by title, artist or album.

**Methods and Paths:** **GET** `/api/tracks/search?query=dark&page=0&size=5`  
**Verification:**
1) Open in browser http://localhost:8080/api/tracks/search?query=dark&page=0&size=5, where:
`query=` is the search term that matches track data,
`page=` is the page number,
`size=` is the number of entries per page.
2) The result of the query is a JSON response showing matching tracks.


*Supporting endpoints:*

- **List tracks** (paginated listing):

**Methods and Paths:** **GET** `/api/tracks?page=0&size=10`  
**Verification:**
1) Open in browser http://localhost:8080/api/tracks?page=0&size=10, where:
`page=` is the page number,
`size=` is the number of entries per page.



## **UC3 - Create Playlist**

Posting valid JSON creates and returns a created playlist JSON.

**Methods and Paths:** **POST** `/api/playlists`  
**Verification:**

1) A playlist can be created via the following snippet in PowerShell:
```powershell
$body = @{
  name     = "New Playlist 1"
  isPublic = $true
  imageUrl = $null
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/playlists" `
  -ContentType "application/json" `
  -Body $body
```

2) Find the JSON output here: http://localhost:8080/api/playlists/1 or view in H2 via:
```sql
SELECT * FROM PLAYLISTS ORDER BY PLAYLIST_ID DESC;
```
Expected JSON output:
```json
{
  "playlistId": 1,
  "name": "New Playlist 1",
  "public": true,
  "imageUrl": null,
  "createdAt": "..."
}
```


*Supporting endpoints:*

- **Get playlist by id** (returns 404 if not found)

**Methods and Paths:** **GET** `/api/playlists/{id}`  
**Verification:**
1) "http://localhost:8080/api/playlists/1"

- **List Playlists** (paginated listing)

**Methods and Paths:** **GET** `/api/playlists`  
**Verification:**
1) Open in browser http://localhost:8080/api/playlists?page=0&size=10, where:
`page=` is the page number,
`size=` is the number of entries per page.



## **UC4 - Delete Playlist**

Returns 204 if the playlist is deleted, 404 if not found.

**Methods and Paths:** **DELETE** `/api/playlists/{id}`  
**Verification:**
1) Create a playlist to be deleted (returns a JSON object with id: 1):

```powershell
$body = @{
  name     = "Temporary Playlist"
  isPublic = $true
  imageUrl = $null
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/playlists" `
  -ContentType "application/json" `
  -Body $body
```
2) Delete the created playlist:
```powershell
Invoke-RestMethod -Method Delete -Uri "http://localhost:8080/api/playlists/1"
```
3) Verify deletion in the browser (should return 404): http://localhost:8080/api/playlists/1