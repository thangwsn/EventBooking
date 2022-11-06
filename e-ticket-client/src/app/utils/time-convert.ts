export function TimeConvert(date: string, time: string) {
    let year = parseInt(date.split("-")[0])
    let month = parseInt(date.split("-")[1]) - 1
    let day = parseInt(date.split("-")[2])
    let hour = parseInt(time.split(":")[0])
    let min = parseInt(time.split(":")[1]);
    return new Date(year, month, day, hour, min).getTime();
}

export function ConvertToDate(time: number) {
    return new Date(time).toLocaleDateString('en-us', { year:"numeric", month:"numeric", day:"numeric", hour: "2-digit", minute: "2-digit", second:"2-digit"}) ;
}