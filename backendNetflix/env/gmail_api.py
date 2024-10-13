from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from google.auth.transport.requests import Request
import os

# Escopos que dão acesso ao Gmail
SCOPES = ["https://www.googleapis.com/auth/gmail.readonly"]

def main():
    # Carregar as credenciais
    creds = None
    if os.path.exists("token.json"):
        creds = Credentials.from_authorized_user_file("token.json", SCOPES)

    # Se não houver credenciais ou elas forem inválidas, faça o login
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file("credentials.json", SCOPES)
            creds = flow.run_local_server(port=8080)  # Use a mesma porta que você registrou
        # Salva as credenciais para as próximas execuções
        with open("token.json", "w") as token:
            token.write(creds.to_json())

    # Conectando à API Gmail
    service = build("gmail", "v1", credentials=creds)

    # Listando emails da caixa de entrada
    results = service.users().messages().list(userId="me", labelIds=["INBOX"]).execute()
    messages = results.get("messages", [])

    if not messages:
        print("Nenhuma mensagem encontrada.")
    else:
        print("Mensagens:")
        for message in messages:
            msg = service.users().messages().get(userId="me", id=message["id"]).execute()
            print(msg["snippet"])

if __name__ == "__main__":
    main()
