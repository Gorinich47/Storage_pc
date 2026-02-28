////// box

// Устанавливает ID для аренды
function rentBoxId(button) {

    const id = button.getAttribute('data-id');

    // Создаем временный элемент для загрузки нового содержимого
    fetch('/box/fragments/account_edit_modal?id='+id)
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            //document.querySelector('#idBox').value = button.getAttribute('data-id-box');

            // Установка выбранных боксов из boxes-edit
            setCheckedBoxes_account();
            // Обновляем счётчик
            updateSelectedCount_account();
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}

// Заполняет форму редактирования бокса
function editBox(button) {

    const id = button.getAttribute('data-id');
    const idBox = button.getAttribute('data-id-box');

    // Создаем временный элемент для загрузки нового содержимого
    fetch('/box/fragments/box_edit_modal?id='+id)
         .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            document.querySelector('#boxId').value = button.getAttribute('data-id');
            document.querySelector('#idBox').value = button.getAttribute('data-id-box');
            document.querySelector('#square').value = button.getAttribute('data-square');
            document.querySelector('#length').value = button.getAttribute('data-length');
            document.querySelector('#width').value = button.getAttribute('data-width');
            document.querySelector('#height').value = button.getAttribute('data-height');
            document.querySelector('#entrance').value = button.getAttribute('data-entrance');
            document.querySelector('#isWarm').checked = button.getAttribute('data-is-warm') === 'true';
            document.querySelector('#isElectricity').checked = button.getAttribute('data-is-electricity') === 'true';
            document.querySelector('#isWater').checked = button.getAttribute('data-is-water') === 'true';
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}

// Очищает форму при добавлении нового
function clearForm_box() {
    document.getElementById('boxId').value = '';
    document.getElementById('idBox').value = '';
    document.getElementById('square').value = '';
    document.getElementById('length').value = '';
    document.getElementById('width').value = '';
    document.getElementById('height').value = '';
    document.getElementById('entrance').value = '';
    document.getElementById('isWarm').checked = false;
    document.getElementById('isElectricity').checked = false;
    document.getElementById('isWater').checked = false;
    document.getElementById('boxModalLabel').textContent = 'Добавить бокс';
}

// Устанавливает ID для удаления
function setDeleteId_box(button) {
    const id = button.getAttribute('data-id');
    const idBox = button.getAttribute('data-id-box');
    //const data-text-form = button.getAttribute('data-text-form');
    document.getElementById('BoxName').textContent = idBox;
    document.getElementById('TextFormVopros').textContent = 'Вы действительно хотите удалить';//data-text-form;
    document.getElementById('confirmDeleteBtn').textContent = 'Удалить';
    document.getElementById('confirmDeleteBtn').href = '/box/delete?id=' + id;
}

///// account
function editAccount(button) {

    const id = button.getAttribute('data-id');
    //const idBox = button.getAttribute('data-id-box');

    // Создаем временный элемент для загрузки нового содержимого
    fetch('/account/fragments/account_edit_modal?id='+id)
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            document.getElementById('accountId').value = button.getAttribute('data-id');
            document.getElementById('dateStart').value = button.getAttribute('data-date-start');
            document.getElementById('dateEnd').value = button.getAttribute('data-date-end');
            document.getElementById('client').value = button.getAttribute('data-client-id');
            document.getElementById('employee').value = button.getAttribute('data-employee-id');
            document.getElementById('statusBox').value = button.getAttribute('data-status');
            document.getElementById('sumAmount').value = button.getAttribute('data-sum-amount');
            //document.querySelectorAll('input[name="boxIds"]').forEach(cb => cb.checked = true);
            //document.getElementById('boxes-form-edit').value = button.getAttribute('data-boxes');
            document.getElementById('accountModalLabel').textContent = 'Редактировать счёт';

            // Установка выбранных боксов из boxes-edit
            setCheckedBoxes_account();
            // Обновляем счётчик
            updateSelectedCount_account();
        }
    )
    .catch(error => console.error('Error loading fragment:', error));

}

function clearForm_account() {
    document.getElementById('accountId').value = '';
    document.getElementById('dateStart').value = '';
    document.getElementById('dateEnd').value = '';
    document.getElementById('client').value = '';
    document.getElementById('employee').value = '';
    document.getElementById('statusBox').value = '';
    document.getElementById('sumAmount').value = '';
    //document.getElementById('boxes-form-edit').value = [];
    //document.querySelectorAll('input[name="boxIds"]').forEach(cb => cb.checked = false);
    document.getElementById('accountModalLabel').textContent = 'Добавить счёт';
    setCheckedBoxesChecked_account(false);
}

function setDeleteId_account(button) {
    const id = button.getAttribute('data-id');
    const clientName = button.getAttribute('data-client-name');
    document.getElementById('deleteClientName').textContent = clientName;
    document.getElementById('confirmDeleteBtn').href = '/account/delete?id=' + id;
}

///// accout_edit_modal
// Обновление счётчика выбранных боксов
function updateSelectedCount_account() {
    const checkedBoxes = document.querySelectorAll('.box-checkbox:checked');
    document.getElementById('selectedCount').textContent = checkedBoxes.length;
}

// Выделение всех/снятие всех чекбоксов
function toggleAllBoxes_account(source) {
    setCheckedBoxesChecked_account(source.checked)
}

function setCheckedBoxesChecked_account(checked){
   const checkboxes = document.querySelectorAll('.box-checkbox');
       checkboxes.forEach(checkbox => {
           checkbox.checked = checked;
       });
       updateSelectedCount_account();
}

// Установка выбранных боксов из boxes-edit
function setCheckedBoxes_account() {
    const boxesEditInput = document.getElementById('boxes-form-edit');

    if (!boxesEditInput || !boxesEditInput.value) return;

    try {
        // Парсим значение из скрытого поля (предполагаем, что это JSON массив ID)
        const selectedBoxIds = JSON.parse(boxesEditInput.value);
        if (!Array.isArray(selectedBoxIds)) return;

        // Находим все чекбоксы и устанавливаем состояние
        const checkboxes = document.querySelectorAll('.box-checkbox');
        checkboxes.forEach(checkbox => {
            if (selectedBoxIds.includes(parseInt(checkbox.value))) {
                checkbox.checked = true;
                console.log('Установлен checkbox:', parseInt(checkbox.value));
            }
        });

        // Обновляем счётчик
        //updateSelectedCount_account();
    } catch (e) {
        console.error('Error parsing boxes-edit value:', e);
    }
}

///// price
// Заполняет форму данными для редактирования
function editPrice(button) {
    const id = button.getAttribute('data-id');
    const boxId = button.getAttribute('data-box-id');
    const sum_price = button.getAttribute('data-sum_price');
    const dateStart = button.getAttribute('data-date-start');
    const dateEnd = button.getAttribute('data-date-end');

    document.getElementById('priceId').value = id;
    document.getElementById('boxId').value = boxId;
    document.getElementById('sum_price').value = sum_price;
    document.getElementById('dateStart').value = dateStart;
    document.getElementById('dateEnd').value = dateEnd;
    document.getElementById('addPriceModalLabel').textContent = 'Редактировать тариф';
}

// Очищает форму при добавлении нового
function clearForm_price() {
    document.getElementById('priceId').value = '';
    document.getElementById('boxId').value = '';
    document.getElementById('sum_price').value = '0.0';
    document.getElementById('dateStart').value = '';
    document.getElementById('dateEnd').value = '';
    document.getElementById('addPriceModalLabel').textContent = 'Добавить новый тариф';
}

// Устанавливает ID для удаления
function setDeleteId_price(button) {
  const id = button.getAttribute('data-id');
  const boxName = button.getAttribute('data-box-name');
  document.getElementById('deleteObjectName').textContent = boxName;
  document.getElementById('confirmDeleteBtn').href = '/price/delete?id=' + id;
}

///// client
// Заполняет форму данными для редактирования
function editClient(button) {

    const id = button.getAttribute('data-id');
    // Создаем временный элемент для загрузки нового содержимого
    fetch('/client/fragments/client_edit_modal?id='+id)
         .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            document.getElementById('clientId').value =  button.getAttribute('data-id');
            document.getElementById('lastName').value =  button.getAttribute('data-lastName');
            document.getElementById('firstName').value =  button.getAttribute('data-firstName');
            document.getElementById('birthDate').value =  button.getAttribute('data-birthDate');
            document.getElementById('phoneNumber').value =  button.getAttribute('data-phoneNumber');
            document.getElementById('emailAddress').value =  button.getAttribute('data-emailAddress');
            document.getElementById('address').value =  button.getAttribute('data-address');
            document.getElementById('comment').value =  button.getAttribute('data-comment');
            document.getElementById('addPriceModalLabel').textContent = 'Редактировать клиента';
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}

// Очищает форму при добавлении нового
function clearForm_client() {
    document.getElementById('clientId').value = '';
    document.getElementById('lastName').value = '';
    document.getElementById('firstName').value = '';
    document.getElementById('birthDate').value = '';
    document.getElementById('phoneNumber').value = '';
    document.getElementById('emailAddress').value = '';
    document.getElementById('address').value = '';
    document.getElementById('comment').value = '';
    document.getElementById('addPriceModalLabel').textContent = 'Добавить нового клиента'
}

// Устанавливает ID для удаления
function setDeleteId_client(button) {
  const id = button.getAttribute('data-id');
  document.getElementById('deleteObjectName').textContent = button.getAttribute('data-client-name');
  document.getElementById('confirmDeleteBtn').href = '/client/delete?id=' + id;
}

// Заполняет форму данными для редактирования
function editEmployee(button) {
    document.getElementById('employeeId').value = button.getAttribute('data-id');
    document.getElementById('firstName').value = button.getAttribute('data-first-name');
    document.getElementById('lastName').value = button.getAttribute('data-last-name');
    document.getElementById('birthDate').value = button.getAttribute('data-birth-date');
    document.getElementById('phoneNumber').value = button.getAttribute('data-phone-number');
    document.getElementById('emailAddress').value = button.getAttribute('data-email');
    document.getElementById('address').value = button.getAttribute('data-address');
    document.getElementById('postEmployee').value = button.getAttribute('data-post');
    document.getElementById('dateStart').value = button.getAttribute('data-date-start');
    document.getElementById('dateEnd').value = button.getAttribute('data-date-end');
    document.getElementById('isFullTime').checked = button.getAttribute('data-is-full-time') === 'true';
    document.getElementById('employeeModalLabel').textContent = 'Редактировать сотрудника';
}

// Очищает форму при добавлении нового
function clearForm_employees(){
    	$('#formEmployees')[0].reset();

}
//function clearForm_employees() {
//    document.getElementById('employeeId').value = '';
//    document.getElementById('firstName').value = '';
//    document.getElementById('lastName').value = '';
//    document.getElementById('birthDate').value = '';
//    document.getElementById('phoneNumber').value = '';
//    document.getElementById('emailAddress').value = '';
//    document.getElementById('address').value = '';
//    document.getElementById('postEmployee').value = '';
//    document.getElementById('dateStart').value = '';
//    document.getElementById('dateEnd').value = '';
//    document.getElementById('isFullTime').checked = false;
//    document.getElementById('employeeModalLabel').textContent = 'Добавить сотрудника';
//}

// Устанавливает ID для удаления
function setDeleteId_employeer(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');
    document.getElementById('deleteName').textContent = name;
    document.getElementById('confirmDeleteBtn').href = '/employee/delete?id=' + id;
}

// Восстановление активной вкладки
document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const openTab = urlParams.get('openTab');
    if (openTab === 'employee') {
        localStorage.setItem('activeTab', '#employee');
    }
    const savedTab = localStorage.getItem('activeTab');
    const triggerEl = savedTab ? document.querySelector(`button[data-bs-target="${savedTab}"]`) : null;
    if (triggerEl) {
        bootstrap.Tab.getOrCreateInstance(triggerEl).show();
    }
});