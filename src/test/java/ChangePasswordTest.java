import com.opencart.helpers.DBHelper;
import com.opencart.helpers.UserHelper;
import com.opencart.navigation.Navigation;
import com.opencart.steps.AdminPageBL;
import com.opencart.steps.ForgottenPageBL;
import com.opencart.steps.MainPageBL;
import com.opencart.steps.RegisterPageBL;
import com.opencart.util.RandomEmailUtil;
import org.testng.annotations.*;

import static com.opencart.enums.URLs.ADMIN_PAGE_URL;
import static com.opencart.enums.URLs.BASE_URL;

public class ChangePasswordTest extends BaseTest {

    private String randomEmail;

    @Test(priority = 1)
    public void registerUserWithValidParameters() {
        new Navigation().navigateToUrl(BASE_URL.getValue());
        MainPageBL mainPageBL = new MainPageBL();
        RegisterPageBL registerPageBL = mainPageBL.getHeaderPageBL()
                .clickOnMyAccountButton()
                .clickOnRegisterButton()
                .registerNewPerson();
        randomEmail = RandomEmailUtil.email;
        registerPageBL.verifyUserRegistration();
    }

    @Test(priority = 2)
    public void unlockUserWithDB() {
        new Navigation().navigateToUrl(BASE_URL.getValue());
        UserHelper.lockUser(randomEmail);
        UserHelper.changePassword(randomEmail);
        DBHelper.unlockUser(randomEmail);
        MainPageBL mainPageBL = new MainPageBL();
        mainPageBL.getHeaderPageBL()
                .clickOnMyAccountButton()
                .clickOnLoginButton()
                .loginUser(randomEmail, ForgottenPageBL.newPassword)
                .verifyUserLogin();
    }

    @Test(priority = 3)
    public void unlockUserWithAdminPanel() {
        Navigation navigation = new Navigation();
        navigation.navigateToUrl(BASE_URL.getValue());
        UserHelper.lockUser(randomEmail);
        UserHelper.changePassword(randomEmail);
        navigation.openNewTab(ADMIN_PAGE_URL.getValue());
        new AdminPageBL().loginAdmin()
                .unlockUser(randomEmail)
                .clickOnMyAccountButton()
                .clickOnLoginButton()
                .loginUser(randomEmail, ForgottenPageBL.newPassword)
                .verifyUserLogin();
    }

    @AfterMethod
    public void logout() {
        new MainPageBL().getHeaderPageBL()
                .clickOnMyAccountButton()
                .clickOnLogoutButton()
                .clickContinue()
                .verifyLogout();
    }
}
