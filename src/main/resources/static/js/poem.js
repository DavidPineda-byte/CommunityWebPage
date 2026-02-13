document.addEventListener("DOMContentLoaded", () => {
    const cards = document.querySelectorAll(".card");

    const modal = document.getElementById("poemModal");
    const titleEl = document.getElementById("modalTitle");
    const authorEl = document.getElementById("modalAuthor");
    const contentEl = document.getElementById("modalContent");

    cards.forEach(card => {
        const desc = card.querySelector(".description");

        // Hover → show description
        card.addEventListener("mouseenter", () => {
            const id = card.dataset.id;

            fetch(`/api/poem/${id}`)
                .then(res => res.json())
                .then(poem => {
                    desc.textContent = poem.description;
                    desc.style.display = "block";
                });
        });

        card.addEventListener("mouseleave", () => {
            desc.style.display = "none";
        });

        // Click → show full poem in modal
        card.addEventListener("click", () => {
            const id = card.dataset.id;

            fetch(`/api/poem/${id}`)
                .then(res => res.json())
                .then(poem => {
                    titleEl.textContent = poem.title;
                    authorEl.textContent = poem.author;
                    contentEl.textContent = poem.body;
                    const bsModal = new bootstrap.Modal(modal);
                    bsModal.show();
                });
        });
    });

    // Click outside modal → close
    modal.addEventListener("click", () => {
        modal.style.display = "none";
    });
});
