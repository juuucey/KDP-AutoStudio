' KDP AutoStudio Silent Launcher (No Console Window)
' This launcher runs the PowerShell script without showing a console window

Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

' Get the directory where this script is located
strScriptPath = objFSO.GetParentFolderName(WScript.ScriptFullName)
strPowerShellScript = strScriptPath & "\Launch KDP AutoStudio.ps1"

' Check if PowerShell script exists
If objFSO.FileExists(strPowerShellScript) Then
    ' Run PowerShell script (hidden window)
    objShell.Run "powershell.exe -ExecutionPolicy Bypass -WindowStyle Hidden -File """ & strPowerShellScript & """", 0, False
Else
    ' Fallback to batch file
    strBatchFile = strScriptPath & "\Launch KDP AutoStudio.bat"
    If objFSO.FileExists(strBatchFile) Then
        objShell.Run """" & strBatchFile & """", 1, False
    Else
        MsgBox "Launcher files not found!", vbCritical, "KDP AutoStudio"
    End If
End If

