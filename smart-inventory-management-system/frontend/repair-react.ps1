# ================================
# React JSX Auto Repair Script
# Keeps everything up to the FIRST "export default"
# Creates .bak backups before modifying files
# ================================

$root = ".\src"
$fixed = @()

Get-ChildItem $root -Recurse -Filter *.jsx | ForEach-Object {

    $file = $_.FullName
    $lines = Get-Content $file

    $exportIndex = -1

    for ($i = 0; $i -lt $lines.Count; $i++) {

        if ($lines[$i] -match '^\s*export\s+default\b') {
            $exportIndex = $i
            break
        }

    }

    if ($exportIndex -ge 0 -and $exportIndex -lt ($lines.Count - 1)) {

        Copy-Item $file "$file.bak" -Force

        $newContent = $lines[0..$exportIndex]

        Set-Content -Path $file -Value $newContent

        $fixed += $file

    }

}

Write-Host ""
Write-Host "=========================================" -ForegroundColor Green
Write-Host "Repair Complete" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
Write-Host ""

if ($fixed.Count -eq 0) {

    Write-Host "No corrupted JSX files were found."

}
else {

    Write-Host "Files repaired:`n" -ForegroundColor Yellow

    $fixed | ForEach-Object {

        Write-Host $_

    }

    Write-Host ""
    Write-Host "Total repaired: $($fixed.Count)" -ForegroundColor Cyan

}