document.getElementById("form-login").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email");
    const senha = document.getElementById("password");
    const pErro = document.getElementById("mensagem-de-erro");

    pErro.textContent("")

    try {
        const resposta = await fetch("http://localhost:8080/funcionarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            },
            body: JSON.stringify({email,senha})
        })

        if (resposta.ok) {
            const dadosUsuario = await resposta.json();
            console.log("Login bem-sucedido:", dadosUsuario);
            localStorage.setItem("UsuarioLogado", JSON.stringify(dadosUsuario))
            window.location.href = "index.html";
        } else {
            const erroJson = await resposta.json();
            pErro.textContent = erroJson.erro || "Email ou senha incorretos."
        }
    } catch (erro) {
        console.error("Erro de conexão: ", erro);
        pErro.textContent = "Nao foi possivel se conectar ao servidor."
    }
})