$ErrorActionPreference = "Stop"

$baseUrl = "http://localhost:8080/api"
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$username = "demo_$timestamp"

Write-Host "Register user..."
$registerBody = @{
    username = $username
    password = "password123"
    password_confirm = "password123"
    email = "$username@example.com"
} | ConvertTo-Json

$registerResp = Invoke-RestMethod -Uri "$baseUrl/auth/register/" -Method Post -ContentType "application/json" -Body $registerBody
$token = $registerResp.token
$headers = @{
    Authorization = "Token $token"
}

Write-Host "Read profile and preferences..."
Invoke-RestMethod -Uri "$baseUrl/auth/profile/" -Method Get -Headers $headers | Out-Null
Invoke-RestMethod -Uri "$baseUrl/auth/preferences/" -Method Get -Headers $headers | Out-Null

Write-Host "Create transaction..."
$txBody = @{
    amount = 125000
    description = "Smoke test transaction"
    category = 2
    transaction_date = (Get-Date -Format "yyyy-MM-dd")
} | ConvertTo-Json
Invoke-RestMethod -Uri "$baseUrl/transactions/" -Method Post -ContentType "application/json" -Headers $headers -Body $txBody | Out-Null

Write-Host "Read dashboard datasets..."
Invoke-RestMethod -Uri "$baseUrl/transactions/?page=1" -Method Get -Headers $headers | Out-Null
Invoke-RestMethod -Uri "$baseUrl/transactions/statistics/?period=all" -Method Get -Headers $headers | Out-Null
Invoke-RestMethod -Uri "$baseUrl/transactions/expenses/" -Method Get -Headers $headers | Out-Null
Invoke-RestMethod -Uri "$baseUrl/categories/" -Method Get -Headers $headers | Out-Null

Write-Host "Read notifications and mark all read..."
$notifications = Invoke-RestMethod -Uri "$baseUrl/notifications/?limit=10" -Method Get -Headers $headers
if ($notifications.results.Count -gt 0) {
    $firstId = $notifications.results[0].id
    Invoke-RestMethod -Uri "$baseUrl/notifications/$firstId/mark_read/" -Method Post -Headers $headers | Out-Null
}
Invoke-RestMethod -Uri "$baseUrl/notifications/mark_all_read/" -Method Post -Headers $headers | Out-Null
Invoke-RestMethod -Uri "$baseUrl/notifications/unread_count/" -Method Get -Headers $headers | Out-Null

Write-Host "Smoke test passed."
