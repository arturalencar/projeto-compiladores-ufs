$cp = "out\etapa2\classes;out\etapa2\gerado"
$main = "quinas.Main"
$raizTestes = Resolve-Path "..\testes"

$resultados = foreach ($categoria in @("sucesso", "falha")) {
  $esperadoPassar = $categoria -eq "sucesso"
  $pasta = Join-Path $raizTestes $categoria

  Get-ChildItem $pasta -File -Filter "*.qui" | Sort-Object Name | ForEach-Object {
      $saida = & java -cp $cp $main $_.FullName 2>&1
      $codigo = $LASTEXITCODE
      $passou = $codigo -eq 0

      [PSCustomObject]@{
          Arquivo  = $_.Name
          Esperado = if ($esperadoPassar) { "sucesso" } else { "falha" }
          Resultado = if ($passou) { "sucesso" } else { "falha" }
          Correto  = $passou -eq $esperadoPassar
          Saida    = ($saida -join "`n")
      }
  }
}

$resultados |
  Select-Object Arquivo, Esperado, Resultado, Correto |
  Format-Table -AutoSize

"`nResumo:"
"Total: $($resultados.Count)"
"Classificados corretamente: $(($resultados | Where-Object Correto).Count)"
"Discordâncias: $(($resultados | Where-Object { -not $_.Correto }).Count)"

$resultados |
    Where-Object { -not $_.Correto } |
    ForEach-Object {
        "`n--- $($_.Arquivo) (esperado: $($_.Esperado), obtido: $($_.Resultado)) ---"
        $_.Saida
    }

$resultados | Export-Csv "out\etapa2\resultado-testes.csv" -NoTypeInformation -Encoding UTF8
