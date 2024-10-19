{{/*
Expand the name of the application by prepending the release name.
*/}}
{{- define "backend-app.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{ .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end -}}
{{- end -}}

{{/*
Return the chart name
*/}}
{{- define "backend-app.name" -}}
{{- .Chart.Name -}}
{{- end -}}

