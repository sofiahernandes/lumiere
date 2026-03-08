import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: {
    default: "Lumière Admin | Projeto 8 - 3CCOMP",
    template: "%s | Lumière Admin",
  },
  description:
    "Aplicação web para gestao de pacientes, exercícios e acompanhamento clinico da fisioterapeuta Maya Yoshiko Yamamoto.",
  applicationName: "Lumière Admin",
  authors: [{ name: "Grupo Lumière - Projeto 8 - 3CCOMP" }],
  creator: "Grupo Lumière",
  metadataBase: new URL("https://Lumière.local"),
  keywords: ["Lumière", "Projeto 8", "3CCOMP", "Fisioterapia", "RPG", "Admin"],
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <head>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link
          rel="preconnect"
          href="https://fonts.gstatic.com"
          crossOrigin=""
        />
        <link
          href="https://fonts.googleapis.com/css2?family=Archivo:ital,wght@0,100..900;1,100..900&family=Raleway:ital,wght@0,100..900;1,100..900&family=Stack+Sans+Headline:wght@200..700&display=swap"
          rel="stylesheet"
        />
      </head>
      <body className="antialiased">{children}</body>
    </html>
  );
}
