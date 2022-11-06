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