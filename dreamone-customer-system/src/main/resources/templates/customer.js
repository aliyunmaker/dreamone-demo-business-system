var customers;

function queryData() {
    $("#customerPage").empty();
    $.ajax({
        url: "../" + "customer/listCustomers",
        type: "POST",
        data: {},
        success: function (result) {
            if (result.success) {
                customers = result.data;
                listCustomers(customers);
                console.log(customers);
            } else {
                console.log("data.message: " + result.errorMsg);
                alert(result.errorMsg);
            }
        },
    })
}

function createData() {
    $.ajax({
        url: "../" + "customer/createCustomers",
        type: "POST",
        data: {},
        success: function (result) {
            if (result.success) {
                document.getElementById('modalData').textContent = "Created customer count: " + result.data;
                // Show the modal
                $('#myModal').modal('show');
                queryData();
                console.log(customers);
            } else {
                console.log("data.message: " + result.errorMsg);
                alert(result.errorMsg);
            }
        },
    })
}

function queryAllData() {
    $("#customerPage").empty();
    $.ajax({
        url: "../" + "customer/listAllCustomers",
        type: "POST",
        data: {},
        success: function (result) {
            if (result.success) {
                document.getElementById('modalData').textContent = "Query cost time: " + result.data + "ms.";
                // Show the modal
                $('#myModal').modal('show');
                console.log(customers);
            } else {
                console.log("data.message: " + result.errorMsg);
                alert(result.errorMsg);
            }
        },
    })
}

function listCustomers(customers) {
    var table = `
    <table class="table mt-2">
        <thead class="table-light">
        <tr>
            <th scope="col">Name</th>
            <th scope="col">Address</th>
            <th scope="col">Phone</th>
            <th scope="col">Account Balance</th>
            <th scope="col">Market Segment</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>`;
    $("#customerPage").append(table);

    for (var customer of customers) {
        var row = `
        <tr>
            <td>${customer.name}</td>
            <td>${customer.address}</td>
            <td>${customer.phone}</td>
            <td>${customer.acctBal}</td>
            <td>${customer.mktSegment}</td>
        </tr>`;
        $("#customerPage tbody").append(row);
    }
}