export class OrganizerCreateRequest {
    constructor(
        public name: string, 
        public email: string,
        public mobile: string, 
        public representative: string, 
        public taxCode: string, 
        public address: string, 
        public summary: string) { }
}

export class OrganizerGet {
    constructor(
        public id: number,
        public name: string, 
        public email: string,
        public mobile: string, 
        public representative: string, 
        public taxCode: string, 
        public address: string,
        public updateBy: string
    ) {}
}