package edu.poly.instagramcloneapp.model
// Introduce: Use for Make: post,user_account

//Success Use:
    //Model:
        //ChungModel:
            // * Update can alter Create
            //* Retrivie : Bấm để nhận kết quả(Search), Fetch Tự hiện kết quả(Profile)
            //* Snapshot có thể kiểm tra và chỉ gọi những gì tồn tại dù trong biến có thứ ko tồn tại
                //Get Username,emai from currentUser: https://stackoverflow.com/questions/59117267/getting-currently-logged-in-users-id-in-firebase-auth-in-kotlin
                //Read Data: https://www.youtube.com/watch?v=_DtbxQ9EEhc&t=812s
                //(Use update) Update Data Firebase: https://www.youtube.com/watch?v=srQ0Nq3mJ_M
                //(Use Create) Upload Profile: https://www.youtube.com/watch?v=UDgMEmqEybc&list=PPSV
                //Update: https://www.youtube.com/watch?v=srQ0Nq3mJ_M&t=139s
                // Retrivie + snapshot Data: https://www.youtube.com/watch?v=7YPbd6gRPyk&list=PLKqtGJUS11t-1s4bd_u6wyuBEEr2fRhMX
                //Retrivie + recylerview: https://www.youtube.com/watch?v=VVXKVFyYQdQ
                //Simple Recyler: https://www.youtube.com/watch?v=Y4S4KNJzmGI
        //PostModel:
            //RecyclerView With Image: https://www.youtube.com/watch?v=0ok8e0JfIoo
            //Upload ảnh: https://www.youtube.com/watch?v=GmpD2DqQYVk
        //MessageModel:
            //Chat Send Message: https://www.youtube.com/watch?v=mycu5zAoox0&t=1s
            //Read Message : https://www.youtube.com/watch?v=ch0TdgXICjQ&t=68s
    //Fragment:
        //Chung:
            // Connect firebase:  https://www.youtube.com/watch?v=YOT8P1PtJQg&t=618s
            //Viewbinding in fragment: https://www.youtube.com/watch?v=v11x54y5YVc
        //SignIn and SignOut : https://www.youtube.com/watch?v=idbxxkF1l6k&list=PLQFUWT9wUMWEXj9AVanMZg1tRUGr95aBr&index=10&t=784s

        //Search:
            //Search RecylerVIew: https://www.youtube.com/watch?v=kGWN_Krbcms
    //Activity:
        //Change start Acitivity: https://www.youtube.com/watch?v=6eES56mxfMs




//During Fixing:
    //Multi Image:
        //+https://stackoverflow.com/questions/46272309/upload-multiple-images-to-firebase-storage
        //+https://sl.bing.net/kgkyaLuz7vM
data class UserModel(
    var uid:String? = null,
    var name:String? = null,
    var email:String? = null,
    var bio:String? = null,
    var imageUrl:String? = null,
)
