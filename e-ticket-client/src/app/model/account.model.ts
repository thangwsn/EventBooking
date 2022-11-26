export class UserLoginRequest {
    constructor(public username: string, public password: string) { }
}

export class UserSignUpRequest {
    constructor(public username: string, public email: string, public password: string) {}
}

export class AccountInfo {
    constructor(
        public fullName: string,
        public mobile: string
    ) {}
}

export class User {
    constructor(
        public id: number,
        public username: string,
        public email: string,
        public mobile: string,
        public fullName: string,
        public amountReserved: number,
        public userCode: string,
    ) {}
}

export class UserDetail {
    constructor( 
        public id: number,
        public username: string,
        public email: string,
        public mobile: string,
        public fullName: string,
        public userCode: string,
        public gender: string,
        public dateOfBirth: number,
        public imageUrl: string,
        public address: string,
        public amountReserved: number,
        public bookingNum: number,
        public listBooking: any[],
        public followedNum: number,
        
    ) {}
}

export class Employee {
    constructor( 
        public id: number,
        public username: string,
        public email: string,
        public mobile: string,
        public fullName: string,
        public employeeCode: string,
        public position: string
    ) {}
}

export class EmployeeDetail {
    constructor( 
        public id: number,
        public username: string,
        public email: string,
        public mobile: string,
        public fullName: string,
        public employeeCode: string,
        public position: string
    ) {}
}