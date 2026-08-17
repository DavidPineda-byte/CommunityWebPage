async function generateImage(prompt) {
    console.log("Starting image purchase workflow with prompt:", prompt);

    if (!prompt) {
        alert("Please enter a prompt first!");
        return;
    }

    // 1. Collect all poem data from the form to save as a temporary draft on our server
    const poemData = {
        title: document.getElementById('title').value,
        author: document.getElementById('author').value,
        genre: document.getElementById('genre').value,
        body: document.getElementById('body').value,
        prompt: prompt
    };

    try {
        console.log("Sending poem draft to server and requesting Stripe session...");
        
        // 2. Send the draft to our local endpoint. 
        // Our controller will save this in TempPoemStorage and return the Stripe URL.
        const response = await fetch('/payment/create-checkout-session', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(poemData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error('Failed to start checkout: ' + response.status + " - " + errorText);
        }

        // 3. Receive the Stripe Checkout URL from our server
        const redirectUrl = await response.text();
        console.log("Redirecting to Stripe:", redirectUrl);

        // 4. Send the user to Stripe to pay
        window.location.href = redirectUrl;

    } catch (error) {
        console.error('Checkout error:', error);
        alert("Error starting checkout. See console for details.");
    }
}

async function pollForImage(tempId) {
    const placeholder = document.getElementById('imagePlaceholder');
    if (placeholder) {
        placeholder.classList.add('d-none');
    }

    const spinner = document.getElementById('imageSpinner');
    if (spinner) {
        spinner.classList.remove('d-none');
    }
    
    console.log("Starting polling for image status, tempId:", tempId);
    const statusInterval = setInterval(async () => {
        try {
            const response = await fetch(`/payment/check-status/${tempId}`);
            if (!response.ok) return;

            const draft = await response.json();

            if (draft && draft.imageUrl) {
                console.log("Image URL found! Updating UI...");
                clearInterval(statusInterval);

                const imgElement = document.getElementById('generatedImage');
                const inputElement = document.getElementById('generatedImageUrlInput');
                const spinner = document.getElementById('imageSpinner');
                const placeholder = document.getElementById('imagePlaceholder');

                if (spinner) {
                    spinner.classList.add('d-none');
                }
                if (placeholder) {
                    placeholder.classList.add('d-none');
                }
                if (imgElement) {
                    imgElement.src = draft.imageUrl;
                    imgElement.style.display = "block";
                }
                if (inputElement) {
                    inputElement.value = draft.imageUrl;
                }
            }
        } catch (error) {
            console.error("Polling error:", error);
        }
    }, 2000);
}

window.onload = () => {
    const urlParams = new URLSearchParams(window.location.search);
    const tempId = urlParams.get('tempId');
    if (tempId) {
        pollForImage(tempId);
    }
};

function validatePoemForm() {
    const inputElement = document.getElementById('generatedImageUrlInput');
    if (!inputElement || !inputElement.value) {
        alert("You must generate an image using our API before saving the poem to the library!");
        return false;
    }
    return true;
}
