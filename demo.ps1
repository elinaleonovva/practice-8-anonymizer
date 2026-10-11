$ErrorActionPreference = "Stop"
$traceId = "trace-demo-001"
$headers = @{ "X-Request-ID" = $traceId }

Write-Host "Starting containers..." -ForegroundColor Cyan
docker compose up --build -d

Write-Host "Waiting for API Gateway..." -ForegroundColor Cyan
for ($attempt = 1; $attempt -le 30; $attempt++) {
    try {
        Invoke-RestMethod "http://localhost/actuator/health" | Out-Null
        break
    } catch {
        if ($attempt -eq 30) { throw "API Gateway did not become available within 60 seconds" }
        Start-Sleep -Seconds 2
    }
}

Write-Host "`n1. Available datasets:" -ForegroundColor Yellow
Invoke-RestMethod "http://localhost/api/datasets" -Headers $headers | ConvertTo-Json -Depth 5

$body = @{ datasetCode = "customers"; columns = @("fullName", "email", "phone") } | ConvertTo-Json
Write-Host "`n2. Creating an export job:" -ForegroundColor Yellow
$job = Invoke-RestMethod "http://localhost/api/exports" -Method Post -Headers $headers `
    -ContentType "application/json" -Body $body
$job | ConvertTo-Json -Depth 5

Start-Sleep -Seconds 2
Write-Host "`n3. Export job status:" -ForegroundColor Yellow
Invoke-RestMethod "http://localhost/api/exports/$($job.id)" -Headers $headers | ConvertTo-Json -Depth 5

Write-Host "`n4. Anonymization result:" -ForegroundColor Yellow
Invoke-RestMethod "http://localhost/api/anonymizations/$($job.id)" -Headers $headers | ConvertTo-Json -Depth 5

Write-Host "`nTrace screenshot command:" -ForegroundColor Cyan
Write-Host "docker compose logs --tail=200 api-gateway dataset-service export-service anonymizer-service | Select-String '$traceId'"
