/* 파일 업로드 기능에서 브라우저 <-> Spring 서버를 연결해주는 중간 역할 */

/* 화면에서 파일을 선택하면 → uploadToServer()가 서버로 파일을 보내고 */
/* → 필요하면 removeFileToServer()가 서버에 저장된 파일을 삭제하는 역할 */


/* 파일을 서버에 업로드하는 함수 */
// 구조 : [사용자 화면] 파일 선택 -> upload.js -> axios -> POST/ upload -> UpDownController -> 파일 저장 -> UploadResultDTO 반환
async function uploadToServer (formObj)
{
    console.log("upload to server......")
    console.log(formObj)

    const response = await axios({ // -> 서버에 HTTP 요청을 보냄 : Controller와 연결되는 부분
        method: 'post',
        url: '/upload/', // 어디로 보낼지 지정 : @PostMapping("/upload") controller와 연결
        data: formObj, // 들어온 파일 데이터
        headers: {
            'Content-Type': 'multipart/form-data', // '파일 업로드' 형태
        },
    });

    return response.data // axios의 응답 객체 전체가 아니라 실제로 서버가 보내준 데이터만 반환
}

/* 서버에 업로드한 파일을 삭제하는 함수 */
// 구조 : [사용자 화면] 파일 삭제 버튼 클릭 -> upload.js -> axios.delete() -> DELETE /upload/{uuid}_{fileName} -> UpDownController -> 서버 파일 삭제
async function removeFileToServer(uuid, fileName)
{
    const response = await axios.delete(`/upload/${uuid}_${fileName}`)
    // -> 서버에 HTTP DELETE 요청을 보냄
    // @DeleteMapping("/{fileName}") controller와 연결

    return response.data
}