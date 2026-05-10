git add .
git status
git commit -m ""
git push -u origin main
git restore --staged .     //for delete files from add to dont show files in git status

🔥 تانيًا: إزاي تشوف كل النسخ القديمة
عرض كل الـ commits:
git log --oneline

هيطلعلك حاجة زي:

a1b2c3 feat(invoice): add invoice module
d4e5f6 feat(client): improve client module



🔙 ثالثًا: الرجوع لنسخة قديمة
✔ مؤقت (مش بيحذف حاجة)
git checkout commit_id

مثال:

git checkout a1b2c3
✔ الرجوع النهائي (تحريك المشروع)
git reset --hard commit_id

مثال:

git reset --hard d4e5f6

⚠️ ده بيمسح التغييرات اللي بعده

✔ طريقة آمنة (مفضلة 👌)
git revert commit_id

👉 ده بيعمل commit جديد يعكس القديم بدون حذف history


1) إنشاء feature branch
git checkout -b feature/client
2) بعد الانتهاء
git push origin feature/client
3) تعمل PR → develop
4) بعد المراجعة → merge
5) لما المشروع يجهز:
develop → main (release)



🧠 3) إزاي تستخدمه في أي Repository

package repository;

import java.sql.Connection;

public class StudentRepository extends BaseRepository {

    public StudentRepository(Connection conn) {
        super(conn);
    }

    // هنا تضيف functions خاصة بالـ Student
    // زي getWhereGrade, getActiveStudents ...
}

🚀 استخدام الـ query()
🟢 مثال 1: بسيط
repo.query("SELECT * FROM students");
🟡 مثال 2: WHERE
repo.query(
    "SELECT * FROM students WHERE age > ?",
    18
);



فايدة Lombok ببساطة إنه بيخلّيك تستغنى عن كتابة الكود المكرر في Java (boilerplate code) زي:

getters
setters
constructors
toString
equals / hashCode









@GetMapping("/profile")
public ApiResponse profile(HttpServletRequest request) {

    Integer userId = (Integer) request.getAttribute("userId");

    if (userId == null) {
        return ApiResponse.error("User not authenticated");
    }

    return ApiResponse.success("User ID = " + userId);
}