$ErrorActionPreference = "Continue"
$traceId = "trace-failure-001"
$headers = @{ "X-Request-ID" = $traceId }
$body = @{ datasetCode = "customers"; columns = @("fullName", "email") } | ConvertTo-Json

Write-Host "Stopping Dataset Service..." -ForegroundColor Yellow
docker compose stop dataset-service
Start-Sleep -Seconds 2

Write-Host "`nRequest during the outage (503 expected):" -ForegroundColor Yellow
$measure = Measure-Command {
    try {
        Invoke-WebRequest "http://localhost/api/exports" -Method Post -Headers $headers `
            -ContentType "application/json" -Body $body -UseBasicParsing
    } catch {
        $response = $_.Exception.Response
        if ($null -ne $response) {
            Write-Host "Received HTTP $([int]$response.StatusCode) $($response.StatusDescription)" -ForegroundColor Green
        } else {
            Write-Host $_.Exception.Message -ForegroundColor Red
        }
    }
}
Write-Host "Elapsed: $([math]::Round($measure.TotalSeconds, 2)) seconds" -ForegroundColor Cyan

Write-Host "`nRestoring Dataset Service..." -ForegroundColor Yellow
docker compose start dataset-service
do {
    Start-Sleep -Seconds 2
    $state = docker compose ps --format json dataset-service | ConvertFrom-Json
} until ($state.Health -eq "healthy")

Write-Host "Dataset Service is restored. A new POST request will be accepted." -ForegroundColor Green
