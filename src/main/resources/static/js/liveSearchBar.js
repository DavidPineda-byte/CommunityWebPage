
class optionService {
    getOptions(){
        throw new Error("Method 'getOptions()' must be implemented");
    }
}

class ListenForInput extends optionService {
    searchBar;

    constructor(){
        super();
        this.searchBar = document.getElementById('searchBar');
    }

    getOptions(){
        return this.searchBar ? this.searchBar.value : '';
    }
}

// @ts-check
class filterInputOptions extends optionService {
    /**
     * @param {optionService} inputToFilter
     */
    constructor(inputToFilter){
        super();
        this.inputToFilter = inputToFilter;
    }

    async getOptions(){
        const inputValue = this.inputToFilter.getOptions();
        if (!inputValue || inputValue.trim() === '') {
            return [];
        }
        return await getOptionsFromAPI(inputValue);
    }
}

// @ts-check
class displayOptions extends optionService {
    /**
     * @param {optionService} getFilteredOptions
     */
    constructor(getFilteredOptions){
        super();
        this.getFilteredOptions = getFilteredOptions;
    }

    async getOptions(){
        const filteredOptions = await this.getFilteredOptions.getOptions();
        const optionsContainer = document.getElementById('optionsContainer');
        if (!optionsContainer) return;
        
        optionsContainer.innerHTML = ''; // Clear previous options

        filteredOptions.forEach(option =>{
            const item = document.createElement('div');
            item.textContent = option.name; // Use option.name to match Name field in Option DTO
            item.style.cursor = 'pointer';
            item.addEventListener('click', () => {
                window.location.href = option.url;
            });
            optionsContainer.appendChild(item);
        });
    }
}

async function getOptionsFromAPI(inputValue){
    try {
        const response = await fetch(`/api/options?input=${inputValue}`); // Added leading slash for root-relative URL
        const data = await response.json();
        return data.options;
    } catch (error) {
        console.error('Error fetching options:', error);
        return [];
    }
}

function searchBarFlow(){
    const listenForInput = new ListenForInput();
    const filterInput = new filterInputOptions(listenForInput);
    const displayFilteredOptions = new displayOptions(filterInput);

    const searchBar = document.getElementById('searchBar');
    if (searchBar) {
        searchBar.addEventListener('input', async () => {
          await displayFilteredOptions.getOptions();
        });
    }
}

// Initialize searchBarFlow on DOMContentLoaded
document.addEventListener('DOMContentLoaded', searchBarFlow);
